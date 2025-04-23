package com.lab6.server;

import com.lab6.server.utility.AskingCommand;
import com.lab6.server.managers.CommandManager;

import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Pair;
import com.lab6.common.utility.Request;
import com.lab6.common.utility.Response;
import com.lab6.common.utility.StandartConsole;
import com.lab6.common.validators.ArgumentValidator;

import com.lab6.server.commands.*;
import com.lab6.server.commands.askingCommands.*;
import com.lab6.server.managers.ServerNetworkManager;
import com.lab6.server.managers.Executer;
import com.lab6.server.managers.CollectionManager;
import com.lab6.server.managers.DumpManager;
import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.net.SocketException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.concurrent.CountDownLatch;

//Вариант 88347
public final class Server {
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    static { initLogger(logger);}
    private static void initLogger(Logger logger) {
        try {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new Formatter() {
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
                            "[" + new java.util.Date(record.getMillis()) + "] " +
                            formatMessage(record) + "\u001B[0m\n";
                }
            });
            // Настройка FileHandler
            FileHandler fileHandler = new FileHandler("server_logs.log", true); // true для добавления логов в конец файла
            fileHandler.setFormatter(new SimpleFormatter()); // Устанавливаем простой форматтер

            // Добавление обработчиков в логгер
            logger.setUseParentHandlers(false);
            logger.addHandler(consoleHandler);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Failed to initialize file handler for logger: " + e.getMessage());
        }
    }

    private static final int PORT = 12345;
    private static final String SERVER_HOST = "localhost";
    private static CommandManager commandManager;
    private static Save saveCommand;
    private static ServerNetworkManager networkManager;
    private static Selector selector;
    private static Response response;
    private static final Console console = new StandartConsole();
    private static CollectionManager collectionManager;

    private static volatile boolean isRunning = true;
    public static void main(String[] args) {

        String filePath = System.getenv("LAB5_FILE_PATH");

        // Проверка наличия и корректности переменной окружения LAB5_FILE_PATH
        if (filePath == null) {
            logger.severe("Environment variable LAB5_FILE_PATH not found!");
            System.exit(1);
        } else if (filePath.isEmpty()) {
            logger.severe("Environment variable LAB5_FILE_PATH does not contain a file path!");
            System.exit(1);
        } else if (!filePath.endsWith(".csv")) {
            logger.severe("The file must be in .csv format!");
            System.exit(1);
        } else if (!new File(filePath).exists()) {
            logger.severe("The file at the specified path was not found!");
            System.exit(1);
        }

        DumpManager dumpManager = new DumpManager(filePath, console);
        collectionManager = new CollectionManager(dumpManager);
        ExecutionStatus loadStatus = collectionManager.loadCollection();
        networkManager = new ServerNetworkManager(PORT, SERVER_HOST);

        // Проверка успешности загрузки коллекции
        if (!loadStatus.isSuccess()) {
            logger.severe(loadStatus.getMessage());
            System.exit(1);
        }
        logger.info("The collection file has been successfully loaded!");
        // Регистрация команд
        commandManager = new CommandManager() {{
            register(CommandNames.HELP.getName(), new Help(this));
            register(CommandNames.INFO.getName(), new Info(collectionManager));
            register(CommandNames.SHOW.getName(), new Show(collectionManager));
            register(CommandNames.ADD.getName(), new Add(collectionManager));
            register(CommandNames.UPDATE.getName(), new Update(collectionManager));
            register(CommandNames.REMOVE_BY_ID.getName(), new RemoveById(collectionManager));
            register(CommandNames.CLEAR.getName(), new Clear(collectionManager));
            register(CommandNames.EXECUTE_SCRIPT.getName(), new ExecuteScript());
            register(CommandNames.EXIT.getName(), new Exit());
            register(CommandNames.REMOVE_FIRST.getName(), new RemoveFirst(collectionManager));
            register(CommandNames.ADD_IF_MIN.getName(), new AddIfMin(collectionManager));
            register(CommandNames.SORT.getName(), new Sort(collectionManager));
            register(CommandNames.REMOVE_ALL_BY_GENRE.getName(), new RemoveAllByGenre(collectionManager));
            register(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), new PrintFieldAscendingDescription(collectionManager));
            register(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), new PrintFieldDescendingDescription(collectionManager));
        }};

        saveCommand = new Save(collectionManager);

        Executer executer = new Executer(commandManager);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                isRunning = false; // Останавливаем цикл
                collectionManager.saveCollection();
                selector.close();
                networkManager.close();
            } catch (Exception e) {
                logger.severe("An error occurred while shutting down the server: " + e.getMessage());
            }
            finally {
                if (logger.getHandlers().length == 0) {
                    initLogger(logger);
                }
                logger.info("Server shutdown complete.");
            }
        }));

        run(executer);
    }




    public static void run(Executer executer) {
        try {
            // Создание селектора для обработки нескольких каналов
            selector = Selector.open();

            // Создание и настройка серверного канала
            networkManager.startServer();

            // Регистрация серверного канала в селекторе для обработки входящих соединений
            networkManager.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);

            logger.info("Selector started");
            logger.info("To stop the server, press [Ctrl + C]");
            while (isRunning) {
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
                                InitialCommandsData(clientChannel, key); //  отправка клиенту списка команд

                            } else if (key.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                Request request;
                                try {
                                    request = networkManager.receive(clientChannel, key);
                                } catch (ServerNetworkManager.NullRequestException | SocketException |
                                         NullPointerException e) {
                                    logger.severe("Error receiving request from client: " + e.getMessage());
                                    logger.info(saveCommand.run("").getMessage());
                                    key.cancel();
                                    continue;
                                }
                                logger.info("Request received from client: " + request);
                                ExecutionStatus executionStatus = executer.runCommand(request.getCommand(), request.getBand());
                                response = new Response(executionStatus);
                                if (!executionStatus.isSuccess()) {
                                    logger.severe(executionStatus.getMessage());
                                } else {
                                    logger.info("Command executed successfully");
                                }
                                clientChannel.register(selector, SelectionKey.OP_WRITE);

                            } else if (key.isWritable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                try {
                                    networkManager.send(response, clientChannel);
                                    logger.info("Response sent to client: " + clientChannel.getRemoteAddress());
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                } catch (IOException e) {
                                    logger.severe("Error sending response to client: " + e.getMessage());
                                    logger.info(saveCommand.run("").getMessage());
                                    key.cancel();
                                }
                            }
                        }
                    } catch (SocketException | CancelledKeyException e) {
                        logger.severe("Client " + key.channel().toString() + " disconnected");
                        logger.info(saveCommand.run("").getMessage());
                        key.cancel();
                    } finally {
                        keys.remove();
                    }
                }
            }
        } catch (ClosedSelectorException e) {
            logger.warning("Selector was closed.");
        } catch (EOFException e) {
            logger.info(saveCommand.run("").getMessage());
            logger.severe(e.getMessage());
            System.exit(1);
        } catch (IOException | NullPointerException | ClassNotFoundException e) {
            logger.severe("Error while running the server: " + e.getMessage());
        }
    }

    private static void InitialCommandsData(SocketChannel clientChannel, SelectionKey key) throws ClosedChannelException {
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        clientChannel.register(selector, SelectionKey.OP_READ);
    }
}