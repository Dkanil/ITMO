package com.lab6.server;

import com.lab6.common.managers.CommandManager;
import com.lab6.common.utility.CommandNames;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Request;
import com.lab6.common.utility.StandartConsole;
import com.lab6.common.utility.Command;

import com.lab6.common.validators.ArgumentValidator;
import com.lab6.server.commands.askingCommands.Add;
import com.lab6.server.commands.askingCommands.AddIfMin;
import com.lab6.server.commands.askingCommands.Update;
import com.lab6.server.commands.Help;
import com.lab6.server.commands.Info;
import com.lab6.server.commands.Show;
import com.lab6.server.commands.RemoveById;
import com.lab6.server.commands.Clear;
import com.lab6.server.commands.Save;
import com.lab6.server.commands.ExecuteScript;
import com.lab6.server.commands.Exit;
import com.lab6.server.commands.RemoveFirst;
import com.lab6.server.commands.Sort;
import com.lab6.server.commands.RemoveAllByGenre;
import com.lab6.server.commands.PrintFieldAscendingDescription;
import com.lab6.server.commands.PrintFieldDescendingDescription;
import com.lab6.server.managers.ServerNetworkManager;

import com.lab6.server.managers.Executer;
import com.lab6.server.managers.CollectionManager;
import com.lab6.server.managers.DumpManager;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.NotSerializableException;
import java.net.SocketException;
import java.nio.channels.*;
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
    private static Console console = new StandartConsole();

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
            register(CommandNames.SAVE.getName(), new Save(collectionManager));
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

            /*
            Request startingMessage = new Request("");
            do {
                try {
                    startingMessage = networkManager.receive(networkManager.getServerSocketChannel());
                    networkManager.send(new Request("Дарова, клиент"), networkManager.getServerSocketChannel());
                    console.println("Клиент подключен: " + startingMessage);
                } catch (NullPointerException e) {
                    console.printError(e.getMessage());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } while (!startingMessage.getCommandString().equals("Дарова, сервер"));
*/
            /*
            for (Command<?> command : commandManager.getCommandsMap().values()) {
                Request request = new Request(command);
                networkManager.send(request, networkManager.getSocketChannel()); // Отправляем клиенту список доступных команд
            }
            networkManager.send(new Request("Отправка команд завершена!"), networkManager.getSocketChannel());
            */

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
                                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                                SocketChannel clientChannel = serverSocketChannel.accept();
                                console.println("Клиент подключен: " + clientChannel.getRemoteAddress());

                                // Настройка канала для неблокирующего режима
                                clientChannel.configureBlocking(false);
                                InitialCommandsData(clientChannel, key);

                            } else if (key.isReadable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                try {
                                    request = networkManager.receive(clientChannel, key);
                                } catch (ClassNotFoundException | IOException e) {
                                    console.printError("Ошибка при получении запроса от клиента: " + e.getMessage());
                                } catch (NullPointerException e) {
                                    console.printError("Клиент отключился от сервера: " + e.getMessage());
                                    key.cancel();
                                }
                                if (request == null) {
                                    console.printError("Запрос от клиента не получен. ПОЧЕМУ?????"); //todo исправить
                                    continue;
                                }
                                console.println("Получен запрос от клиента: " + request);
                                ExecutionStatus validationStatus = executer.validateCommand(request.getCommand());
                                if (!validationStatus.isSuccess()) {
                                    request = new Request(validationStatus.getMessage());
                                    console.printError(validationStatus.getMessage());
                                }
                                else {
                                    ExecutionStatus executionStatus = executer.runCommand(request.getCommand(), request.getBand());
                                    request = new Request(executionStatus.getMessage());
                                    if (!executionStatus.isSuccess()) {
                                        console.printError(executionStatus.getMessage());
                                    } else {
                                        console.println("Команда выполнена успешно: " + executionStatus.getMessage());
                                    }
                                }
                                clientChannel.register(selector, SelectionKey.OP_WRITE);

                            } else if (key.isWritable()) {
                                SocketChannel clientChannel = (SocketChannel) key.channel();
                                clientChannel.configureBlocking(false);

                                try {
                                    networkManager.send(request, clientChannel);
                                    console.println("Ответ отправлен клиенту: " + clientChannel.getRemoteAddress());
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                } catch (IOException e) {
                                    console.printError("Ошибка при отправке ответа клиенту: " + e.getMessage());
                                } catch (NullPointerException e) {
                                    console.printError("Клиент отключился от сервера: " + e.getMessage());
                                    key.cancel();
                                }
                            }
                        }
                    } catch (SocketException | CancelledKeyException e) {
                        console.printError("Клиент " + key.channel().toString() + " отключился");
                        key.cancel();
                    } finally {
                        keys.remove(); // todo gpt насрал
                        //selector.wakeup();
                    }
                }
            }
        } catch (EOFException e) {
            console.printError("Клиент отключился от сервера: " + e.getMessage());
        } catch (NotSerializableException e) {
            console.printError("Объект не сериализуемый (СУКААААААААААААААА): " + e.getMessage());
        } catch (IOException e) {
            console.printError("Ошибка при запуске сервера: " + e.getMessage());
        } catch (NullPointerException e) {
            console.printError("СУКАПИЗДАЗАЕБАЛА" + e.getMessage());
        } catch (ClassNotFoundException e) {
            console.printError("Хз что произошло: " + e.getMessage()); // todo исправить
        }

    }

    private static void InitialCommandsData(SocketChannel clientChannel, SelectionKey key) throws ClosedChannelException {
        try {
            Map<String, ArgumentValidator> commandsData = new HashMap<>();
            for (Map.Entry<String, Command<?>> pair : commandManager.getCommandsMap().entrySet()) {
                commandsData.put(pair.getKey(), pair.getValue().getArgumentValidator());
            }
            networkManager.send(new Request(commandsData), clientChannel); //todo понять как это работает
            console.println("Клиенту отправлен список команд: " + clientChannel.getRemoteAddress());
            clientChannel.register(selector, SelectionKey.OP_READ);
        } catch (NotSerializableException e) {
            console.printError("ЕБАНЫЙ ДОЛБАЁБ НЕ СЕРИАЛИЗОВАЛ ХУЙНЮ: " + e.getMessage()); // todo убрать
            key.cancel();
        } catch (IOException e) {
            console.printError("Ошибка при отправке клиенту списка команд: " + e.getMessage());
            key.cancel();
        } catch (NullPointerException e) {
            console.printError("Клиент отключился от сервера: " + e.getMessage());
            key.cancel();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        clientChannel.register(selector, SelectionKey.OP_READ);
    }
}