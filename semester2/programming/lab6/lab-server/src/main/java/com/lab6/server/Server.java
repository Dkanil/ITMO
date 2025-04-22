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

//Вариант 88347
public final class Server {
    private static final int PORT = 12345;
    private static final String SERVER_HOST = "localhost";
    private static CommandManager commandManager;
    private static ServerNetworkManager networkManager;
    private static Selector selector;
    private static Request request;
    private static Response response;
    private static final Console console = new StandartConsole();

    public static void main(String[] args) {

        String filePath = System.getenv("LAB5_FILE_PATH");

        // Проверка наличия и корректности переменной окружения LAB5_FILE_PATH
        if (filePath == null) {
            console.printError("Переменная окружения LAB5_FILE_PATH не найдена!");
            System.exit(1);
        } else if (filePath.isEmpty()) {
            console.printError("Переменная окружения LAB5_FILE_PATH не содержит пути к файлу!");
            System.exit(1);
        } else if (!filePath.endsWith(".csv")) {
            console.printError("Файл должен быть формата .csv!");
            System.exit(1);
        } else if (!new File(filePath).exists()) {
            console.printError("Файл по указанному пути не найден!");
            System.exit(1);
        }

        DumpManager dumpManager = new DumpManager(filePath, console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);
        ExecutionStatus loadStatus = collectionManager.loadCollection();
        networkManager = new ServerNetworkManager(PORT, SERVER_HOST);

        // Проверка успешности загрузки коллекции
        if (!loadStatus.isSuccess()) {
            console.printError(loadStatus.getMessage());
            System.exit(1);
        }

        // Регистрация команд
        commandManager = new CommandManager() {{
            register(CommandNames.HELP.getName(), new Help(this));
            register(CommandNames.INFO.getName(), new Info(collectionManager));
            register(CommandNames.SHOW.getName(), new Show(collectionManager));
            register(CommandNames.ADD.getName(), new Add(console, collectionManager));
            register(CommandNames.UPDATE.getName(), new Update(console, collectionManager));
            register(CommandNames.REMOVE_BY_ID.getName(), new RemoveById(collectionManager));
            register(CommandNames.CLEAR.getName(), new Clear(collectionManager));
            register(CommandNames.EXECUTE_SCRIPT.getName(), new ExecuteScript());
            register(CommandNames.EXIT.getName(), new Exit());
            register(CommandNames.REMOVE_FIRST.getName(), new RemoveFirst(collectionManager));
            register(CommandNames.ADD_IF_MIN.getName(), new AddIfMin(console, collectionManager));
            register(CommandNames.SORT.getName(), new Sort(collectionManager));
            register(CommandNames.REMOVE_ALL_BY_GENRE.getName(), new RemoveAllByGenre(collectionManager));
            register(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), new PrintFieldAscendingDescription(collectionManager));
            register(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), new PrintFieldDescendingDescription(collectionManager));
        }};

        Executer executer = new Executer(console, commandManager);

        run(console, executer);
    }

    public static void run(Console console, Executer executer) {
        try {
            // Создание селектора для обработки нескольких каналов
            selector = Selector.open();

            // Создание и настройка серверного канала
            networkManager.startServer();

            // Регистрация серверного канала в селекторе для обработки входящих соединений
            networkManager.getServerSocketChannel().register(selector, SelectionKey.OP_ACCEPT);

            console.println("Сервер запущен на " + SERVER_HOST + ":" + PORT);

            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    console.println("Обработка ключа: " + key);
                    try {
                        if (key.isValid()) {
                            if (key.isAcceptable()) {
                                // Принимаем новое соединение
                                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel(); //todo try-with-resources
                                SocketChannel clientChannel = serverSocketChannel.accept();
                                console.println("Клиент подключен: " + clientChannel.getRemoteAddress());

                                // Настройка канала для неблокирующего режима
                                clientChannel.configureBlocking(false);
                                InitialCommandsData(clientChannel, key); //  отправка клиенту списка команд

                            } else if (key.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                try {
                                    request = networkManager.receive(clientChannel, key);
                                } catch (ServerNetworkManager.NullRequestException | SocketException | NullPointerException e) {
                                    console.printError("Ошибка при получении запроса от клиента: " + e.getMessage());
                                    key.cancel();
                                    continue;//todo проверить обработку ошибок
                                }
                                console.println("Получен запрос от клиента: " + request);
                                ExecutionStatus executionStatus = executer.runCommand(request.getCommand(), request.getBand());
                                response = new Response(executionStatus.getMessage());
                                if (!executionStatus.isSuccess()) {
                                    console.printError(executionStatus.getMessage());
                                } else {
                                    console.println("Команда выполнена успешно");
                                }

                                clientChannel.register(selector, SelectionKey.OP_WRITE);

                            } else if (key.isWritable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                try {
                                    networkManager.send(response, clientChannel);
                                    console.println("Ответ отправлен клиенту: " + clientChannel.getRemoteAddress());
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                } catch (IOException e) {
                                    console.printError("Ошибка при отправке ответа клиенту: " + e.getMessage());
                                    key.cancel();
                                } catch (NullPointerException e) {
                                    console.printError("Клиент отключился от сервера: " + e.getMessage());
                                    key.cancel();
                                }
                            }
                        }
                    } catch (SocketException | CancelledKeyException e) {
                        console.printError("Клиент " + key.channel().toString() + " отключился");//todo try-with-resources
                        executer.runCommand(new String[]{"save"}, null);
                        key.cancel();
                    } finally {
                        keys.remove();
                    }
                }
            }
        } catch (EOFException e) {
            console.printError("Клиент отключился от сервера: " + e.getMessage());
        } catch (IOException e) {
            console.printError("Ошибка при запуске сервера: " + e.getMessage());
        } catch (NullPointerException e) {
            console.printError("Ошибка хз чего" + e.getMessage());
        } catch (ClassNotFoundException e) {
            console.printError("Хз что произошло: " + e.getMessage()); // todo исправить
        }

    }

    private static void InitialCommandsData(SocketChannel clientChannel, SelectionKey key) throws ClosedChannelException {
        try {
            Map<String, Pair<ArgumentValidator, Boolean>> commandsData = new HashMap<>();
            for (Map.Entry<String, Command<?>> entrySet : commandManager.getCommandsMap().entrySet()) {
                boolean isAskingCommand = AskingCommand.class.isAssignableFrom(entrySet.getValue().getClass());
                commandsData.put(entrySet.getKey(), new Pair<>(entrySet.getValue().getArgumentValidator(), isAskingCommand));
            }
            networkManager.send(new Response(commandsData), clientChannel); //todo понять как это работает
            console.println("Клиенту отправлен список команд: " + clientChannel.getRemoteAddress());
            clientChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            console.printError("Ошибка при отправке клиенту списка команд: " + e.getMessage());
            key.cancel();
        } catch (NullPointerException e) {
            console.printError("Клиент отключился от сервера: " + e.getMessage());
            key.cancel();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        clientChannel.register(selector, SelectionKey.OP_READ); //todo убрать мб
    }
}