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
import java.net.SocketException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
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

//Вариант 88347
public final class Server {
    public static final Logger logger = Logger.getLogger(Server.class.getName());
    static {
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
                return color + "[" + record.getLevel() + "] " + formatMessage(record) + "\u001B[0m\n";
            }
        });
        logger.setUseParentHandlers(false);
        logger.addHandler(consoleHandler);
    }
    private static final int PORT = 12345;
    private static final String SERVER_HOST = "localhost";
    private static CommandManager commandManager;
    private static Save saveCommand;
    private static ServerNetworkManager networkManager;
    private static Selector selector;
    private static Response response;
    private static final Console console = new StandartConsole();

    public static void main(String[] args) {

        String filePath = System.getenv("LAB5_FILE_PATH");

        // Проверка наличия и корректности переменной окружения LAB5_FILE_PATH
        if (filePath == null) {
            logger.severe("Переменная окружения LAB5_FILE_PATH не найдена!");
            System.exit(1);
        } else if (filePath.isEmpty()) {
            logger.severe("Переменная окружения LAB5_FILE_PATH не содержит пути к файлу!");
            System.exit(1);
        } else if (!filePath.endsWith(".csv")) {
            logger.severe("Файл должен быть формата .csv!");
            System.exit(1);
        } else if (!new File(filePath).exists()) {
            logger.severe("Файл по указанному пути не найден!");
            System.exit(1);
        }

        DumpManager dumpManager = new DumpManager(filePath, console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);
        ExecutionStatus loadStatus = collectionManager.loadCollection();
        networkManager = new ServerNetworkManager(PORT, SERVER_HOST);

        // Проверка успешности загрузки коллекции
        if (!loadStatus.isSuccess()) {
            logger.severe(loadStatus.getMessage());
            System.exit(1);
        }
        logger.info("Файл коллекции успешно загружен!");
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

            logger.info("Селектор запущен");
            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    logger.info("Обработка ключа: " + key);
                    try {
                        //todo разобраться
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                // Принимаем новое соединение
                                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                                SocketChannel clientChannel = serverSocketChannel.accept();
                                logger.info("Клиент подключен: " + clientChannel.getRemoteAddress());

                                // Настройка канала для неблокирующего режима
                                clientChannel.configureBlocking(false);
                                InitialCommandsData(clientChannel, key); //  отправка клиенту списка команд

                            } else if (key.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                Request request;
                                try {
                                    request = networkManager.receive(clientChannel, key);
                                } catch (ServerNetworkManager.NullRequestException | SocketException | NullPointerException e) {
                                    logger.severe("Ошибка при получении запроса от клиента: " + e.getMessage());
                                    logger.info(saveCommand.run("").getMessage());
                                    key.cancel();
                                    continue;
                                }
                                logger.info("Получен запрос от клиента: " + request);
                                ExecutionStatus executionStatus = executer.runCommand(request.getCommand(), request.getBand());
                                response = new Response(executionStatus);
                                if (!executionStatus.isSuccess()) {
                                    logger.severe(executionStatus.getMessage());
                                } else {
                                    logger.info("Команда выполнена успешно");
                                }
                                clientChannel.register(selector, SelectionKey.OP_WRITE);

                            } else if (key.isWritable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                try {
                                    networkManager.send(response, clientChannel);
                                    logger.info("Ответ отправлен клиенту: " + clientChannel.getRemoteAddress());
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                } catch (IOException e) {
                                    logger.severe("Ошибка при отправке ответа клиенту: " + e.getMessage());
                                    logger.info(saveCommand.run("").getMessage());
                                    key.cancel();
                                }
                            }
                        }
                    } catch (SocketException | CancelledKeyException e) {
                        logger.severe("Клиент " + key.channel().toString() + " отключился");
                        logger.info(saveCommand.run("").getMessage());
                        key.cancel();
                    } finally {
                        keys.remove();
                    }
                }
            }
        } catch (EOFException e) {
            logger.info(saveCommand.run("").getMessage());
            logger.severe(e.getMessage());
            System.exit(1);
        }
        catch (IOException | NullPointerException | ClassNotFoundException e) {
            logger.severe("Ошибка при работе сервера: " + e.getMessage());
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
            logger.info("Клиенту отправлен список команд: " + clientChannel.getRemoteAddress());
        } catch (IOException e) {
            logger.severe("Ошибка при отправке клиенту списка команд: " + e.getMessage());
            key.cancel();
        } catch (NullPointerException e) {
            logger.severe("Клиент отключился от сервера: " + e.getMessage());
            key.cancel();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        clientChannel.register(selector, SelectionKey.OP_READ);
    }
}