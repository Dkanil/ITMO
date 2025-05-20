package com.lab7.server;

import com.lab7.server.commands.*;
import com.lab7.server.commands.askingCommands.Add;
import com.lab7.server.commands.askingCommands.AddIfMin;
import com.lab7.server.commands.askingCommands.Update;
import com.lab7.server.managers.CollectionManagerProxy;
import com.lab7.server.managers.CollectionManager;
import com.lab7.server.managers.CommandManager;
import com.lab7.server.managers.DBManager;
import com.lab7.server.managers.Executer;
import com.lab7.server.managers.ServerNetworkManager;
import com.lab7.server.utility.AskingCommand;
import com.lab7.server.utility.Command;
import com.lab7.server.utility.CommandNames;

import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.common.utility.Request;
import com.lab7.common.utility.Response;
import com.lab7.common.validators.ArgumentValidator;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

//Вариант 7995
public final class Server {
    public static final Logger logger = Logger.getLogger(Server.class.getName());

    static {
        initLogger();
    }

    private static void initLogger() {
        try {
            // Настройка ConsoleHandler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(getFormatter());

            // Настройка FileHandler
            FileHandler fileHandler = new FileHandler("server_logs.log", true); // true для добавления логов в конец файла
            fileHandler.setFormatter(new SimpleFormatter()); // Устанавливаем простой форматтер

            // Добавление обработчиков в логгер
            Server.logger.setUseParentHandlers(false);
            Server.logger.addHandler(consoleHandler);
            Server.logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize file handler for logger: " + e.getMessage());
        }
    }

    private static Formatter getFormatter() {
        return new Formatter() {
            @Override
            public String format(LogRecord record) {
                String color = switch (record.getLevel().getName()) {
                    case "SEVERE" -> "\u001B[31m"; // Красный
                    case "WARNING" -> "\u001B[33m"; // Желтый
                    case "INFO" -> "\u001B[32m"; // Зеленый
                    default -> "\u001B[0m"; // Сброс цвета
                };
                return color + "[" + record.getLevel() + "] " +
                        "[" + Thread.currentThread().getName() + "] " +
                        "[" + new Date(record.getMillis()) + "] " +
                        formatMessage(record) + "\u001B[0m\n";
            }
        };
    }

    private static final int PORT = 13876;
    private static CommandManager commandManager;
    private static ServerNetworkManager networkManager;
    private static Selector selector;
    private static final Map<SocketChannel, CompletableFuture<Response>> responseFutures = new ConcurrentHashMap<>();
    private static Executer executer;
    private static final CollectionManager collectionManager = CollectionManagerProxy.getInstance();

    public static void main(String[] args) {

        ExecutionStatus loadStatus = collectionManager.loadCollection();
        networkManager = new ServerNetworkManager(PORT);

        // Проверка успешности загрузки коллекции
        if (!loadStatus.isSuccess()) {
            logger.severe(loadStatus.getMessage());
            System.exit(1);
        }
        logger.info("The collection file has been successfully loaded!");
        // Регистрация команд
        commandManager = new CommandManager() {{
            register(CommandNames.HELP.getName(), new Help(this));
            register(CommandNames.INFO.getName(), new Info());
            register(CommandNames.SHOW.getName(), new Show());
            register(CommandNames.ADD.getName(), new Add());
            register(CommandNames.UPDATE.getName(), new Update());
            register(CommandNames.REMOVE_BY_ID.getName(), new RemoveById());
            register(CommandNames.CLEAR.getName(), new Clear());
            register(CommandNames.EXECUTE_SCRIPT.getName(), new ExecuteScript());
            register(CommandNames.EXIT.getName(), new Exit());
            register(CommandNames.REMOVE_FIRST.getName(), new RemoveFirst());
            register(CommandNames.ADD_IF_MIN.getName(), new AddIfMin());
            register(CommandNames.SORT.getName(), new Sort());
            register(CommandNames.REMOVE_ALL_BY_GENRE.getName(), new RemoveAllByGenre());
            register(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), new PrintFieldAscendingDescription());
            register(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), new PrintFieldDescendingDescription());
        }};

        executer = new Executer(commandManager);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                selector.close();
                networkManager.close();
            } catch (Exception e) {
                logger.severe("An error occurred while shutting down the server: " + e.getMessage());
            }
            finally {
                if (logger.getHandlers().length == 0) {
                    initLogger();
                }
                logger.info("Server shutdown complete.");
                for (var handler : logger.getHandlers()) {
                    handler.close();
                }
            }
        }));

        run();
    }

    public static void run() {
        ForkJoinPool readPool = new ForkJoinPool();
        ForkJoinPool writePool = new ForkJoinPool();

        try {
            // Создание селектора для обработки нескольких каналов
            selector = Selector.open();

            // Создание и настройка серверного канала
            networkManager.startServer();

            // Регистрация серверного канала в селекторе для обработки входящих соединений
            networkManager.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);

            logger.info("Selector started");
            logger.info("To stop the server, press [Ctrl + C]");
            while (true) {
                selector.select();

                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    logger.info("Processing key: " + key);
                    try {
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                // Принимаем новое соединение
                                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                                SocketChannel clientChannel = serverSocketChannel.accept();
                                logger.info("Client connected: " + clientChannel.getRemoteAddress());

                                // Настройка канала для неблокирующего режима
                                clientChannel.configureBlocking(false);

                                Thread processRequestThread = new Thread(() -> {
                                    initialCommandsData(clientChannel, key); //  отправка клиенту списка команд
                                });
                                processRequestThread.start();
                                try {
                                    processRequestThread.join();
                                } catch (InterruptedException e) {
                                    logger.severe("Error while waiting for request processing: " + e.getMessage());
                                }

                                clientChannel.register(selector, SelectionKey.OP_READ);
                            } else if (key.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);
                                if (responseFutures.get(clientChannel) != null) {
                                    continue; // Пропускаем итерацию, если уже идёт обработка этого клиента
                                }
                                CompletableFuture<Response> responseFuture = new CompletableFuture<>();
                                readPool.submit(() -> {
                                    try {
                                        Request request = networkManager.receive(clientChannel, key);
                                        logger.info("Request received from client: " + request);
                                        Thread processRequestThread = getProcessRequestThread(request, key, responseFuture);
                                        processRequestThread.start();
                                    } catch (ServerNetworkManager.NullRequestException | SocketException | NullPointerException e) {
                                        logger.severe("Error receiving request from client: " + e.getMessage());
                                        key.cancel();
                                    } catch (IOException | ClassNotFoundException e) {
                                        logger.severe("Fatal error: " + e.getMessage());
                                        throw new RuntimeException(e);
                                    }
                                });
                                responseFutures.put(clientChannel, responseFuture);
                                clientChannel.register(selector, SelectionKey.OP_WRITE);
                            } else if (key.isWritable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                writePool.submit(() -> {
                                    try {
                                        CompletableFuture<Response> responseFuture = responseFutures.get(clientChannel);
                                        if (responseFuture != null) {
                                            Response response = responseFuture.get();
                                            networkManager.send(response, clientChannel);
                                            logger.info("Response sent to client: " + clientChannel.getRemoteAddress());
                                            //clientChannel.register(selector, SelectionKey.OP_READ);
                                            responseFutures.remove(clientChannel);
                                        }
                                        else{
                                            logger.severe("No response future found for client: " + clientChannel.getRemoteAddress());
                                        }
                                    }  catch (IOException e) {
                                        logger.severe("Error sending response to client: " + e.getMessage());
                                        key.cancel();
                                    } catch (ExecutionException | InterruptedException e) {
                                        logger.severe("Error doing the command: " + e.getMessage());
                                        throw new RuntimeException(e);
                                    }
                                });
                                clientChannel.register(selector, SelectionKey.OP_READ);
                            }
                        }
                    } catch (SocketException | CancelledKeyException e) {
                        try (var channel = key.channel()) {
                            logger.severe("Client " + channel.toString() + " disconnected");
                            key.cancel();
                        }
                    } finally {
                        keys.remove();
                    }
                }
            }
        } catch (ClosedSelectorException e) {
            logger.warning("Selector was closed.");
        } catch (EOFException e) {
            logger.severe(e.getMessage());
            System.exit(1);
        } catch (IOException | NullPointerException e) {
            logger.severe("Error while running the server: " + e.getMessage());
        }
    }

    private static Thread getProcessRequestThread(Request request, SelectionKey key, CompletableFuture<Response> responseFuture) {
        return new Thread(() -> {
            try {
                Response response = processRequest(request);
                responseFuture.complete(response);
            } catch (ClosedChannelException e) {
                logger.severe("Error processing request: " + e.getMessage());
                responseFuture.completeExceptionally(e);
                key.cancel();
            }
        });
    }

    private static Response processRequest(Request request) throws ClosedChannelException {
        if (request.getCommand()[0].equals("register") || request.getCommand()[0].equals("login")) {
            ExecutionStatus authStatus = "register".equals(request.getCommand()[0])
                    ? DBManager.getInstance().addUser(request.getUser())
                    : DBManager.getInstance().checkPassword(request.getUser());
            if (authStatus.isSuccess()) {
                logger.info(authStatus.getMessage() + " User: " + request.getUser().getFirst());
            } else {
                logger.warning(authStatus.getMessage());
            }
            return new Response(authStatus);
        }
        else {
            ExecutionStatus executionStatus = executer.runCommand(request.getCommand(), request.getBand(), request.getUser());
            if (!executionStatus.isSuccess()) {
                logger.severe(executionStatus.getMessage());
            } else {
                logger.info("Command executed successfully");
            }
            return new Response(executionStatus);
        }
    }

    private static synchronized void initialCommandsData(SocketChannel clientChannel, SelectionKey key) {
        try {
            Map<String, Pair<ArgumentValidator, Boolean>> commandsData = new HashMap<>();
            for (Map.Entry<String, Command<?>> entrySet : commandManager.getCommandsMap().entrySet()) {
                boolean isAskingCommand = AskingCommand.class.isAssignableFrom(entrySet.getValue().getClass());
                commandsData.put(entrySet.getKey(), new Pair<>(entrySet.getValue().getArgumentValidator(), isAskingCommand));
            }
            networkManager.send(new Response(commandsData), clientChannel);
            logger.info("Command list sent to the client: " + clientChannel.getRemoteAddress());
        } catch (IOException e) {
            logger.severe("Error sending command list to the client: " + e.getMessage());
            key.cancel();
        } catch (NullPointerException e) {
            logger.severe("The client disconnected from the server: " + e.getMessage());
            key.cancel();
        }
    }
}