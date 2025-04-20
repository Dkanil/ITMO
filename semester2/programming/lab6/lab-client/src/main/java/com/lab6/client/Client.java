package com.lab6.client;

import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Request;
import com.lab6.common.utility.StandartConsole;
import com.lab6.common.utility.Console;
import com.lab6.common.validators.ArgumentValidator;

import java.io.IOException;
import java.net.ConnectException;
import java.util.Map;

//Вариант 88347
public final class Client {
    private static final Console console = new StandartConsole();
    private static final int SERVER_PORT = 12345;
    private static final String SERVER_HOST = "localhost";
    private static Map<String, ArgumentValidator> commandsData;
    private static NetworkManager networkManager = new NetworkManager(SERVER_PORT, SERVER_HOST);
    private static int scriptStackCounter = 0;

    public static void main(String[] args) {

        try {
            networkManager.connect();

            console.println("Connected to " + SERVER_HOST + ":" + SERVER_PORT);

            // todo чекнуть мб исправить
            Request newCommand = networkManager.receive();
            while (newCommand == null) {
                newCommand = networkManager.receive();
            }
            commandsData = newCommand.getCommandsMap();
            console.println("АХУЕТЬ ПОЛУЧИЛОСЬ");// todo нахуя


            while (true) {
                console.println("Введите команду:");
                String inputCommand = console.readln();
                ExecutionStatus validationStatus = validateCommand((inputCommand.trim() + " ").split(" ", 2));
                if (!validationStatus.isSuccess()) {
                    console.printError(validationStatus.getMessage());
                }
                else {
                    Request request = new Request(inputCommand);
                    networkManager.send(request);

                    Request response = networkManager.receive();
                    console.println(response.getCommandString());
                }
            }

        } catch (ConnectException e) {
            console.printError("Не удалось подключиться к серверу. Проверьте, запущен ли сервер и доступен ли он по адресу " + SERVER_HOST + ":" + SERVER_PORT);
//            try { todo мб нужно сделать попытки переподключения
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
        } catch (IOException | ClassNotFoundException e) {
            console.printError("Ошибка при работе с сервером: " + e.getMessage());
        }

    }

    public static ExecutionStatus validateCommand(String[] userCommand) {
        try {
            if (userCommand[0].equals("execute_script")) {
                return new ExecutionStatus(true, "Введена команда 'execute_script'. Валидация аргументов не требуется.");
            } else {
                var argumentValidator = commandsData.get(userCommand[0]);
                if (argumentValidator == null) {
                    return new ExecutionStatus(false, "Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'.");
                } else {
                    return argumentValidator.validate(userCommand[1], userCommand[0]);
                }
            }
        } catch (NullPointerException e) {
            return new ExecutionStatus(false, "Введено недостаточно аргументов для выполнения последней команды!");
        }
        catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выполнении команды!");
        }
    }
/*
    public ExecutionStatus runScript(String fileName) {
        try {
            scriptStackCounter++;
            if (scriptStackCounter > 100) {
                scriptStackCounter--;
                return new ExecutionStatus(false, "Превышена максимальная глубина рекурсии!");
            }
            if (fileName.isEmpty()) {
                scriptStackCounter--;
                return new ExecutionStatus(false, "У команды execute_script должен быть только один аргумент!\nПример корректного ввода: execute_script file_name");
            }
            console.println("Запуск скрипта '" + fileName + "'");
            try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
                Console FileConsole = new FileConsole(input);
                // Подменяем консоль для команд, которые требуют построчного ввода пользователя
                for (var i : commandManager.getCommandsMap().values()) {
                    if (i instanceof AskingCommand) { //todo проверить точно ли работает
                        ((AskingCommand<?>) i).updateConsole(FileConsole);
                    }
                }
                while (scriptStackCounter > 0) {
                    String line = input.readLine();
                    if (!line.equals("exit")) {
                        String[] inputCommand = (line.trim() + " ").split(" ", 2);
                        inputCommand[1] = inputCommand[1].trim();


                        ExecutionStatus commandStatus = runCommand(inputCommand, null); // todo поменять на метод отправки запроса

                        if (commandStatus.isSuccess()) {
                            console.println(commandStatus.getMessage());
                        } else {
                            if (!commandStatus.getMessage().equals("Выполнение скрипта приостановлено.")) {
                                console.printError(commandStatus.getMessage());
                            }
                            return new ExecutionStatus(false, "Выполнение скрипта приостановлено.");
                        }
                    } else {
                        scriptStackCounter--;
                        return new ExecutionStatus(true, "Скрипт успешно выполнен.");
                    }
                }
            } catch (FileNotFoundException e) {
                return new ExecutionStatus(false, "Не удаётся найти файл скрипта!");
            } catch (IllegalArgumentException e) {
                return new ExecutionStatus(false, "Произошла ошибка при чтении данных из файла скрипта!");
            } catch (Exception e) {
                return new ExecutionStatus(false, "Произошла ошибка при выполнении команды скрипта!");
            }
            return new ExecutionStatus(true, "");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при запуске скрипта!");
        }
        finally {
            // Возвращаем консоль для команд в исходное состояние
            for (var i : commandManager.getCommandsMap().values()) {
                if (i instanceof AskingCommand) {
                    ((AskingCommand<?>) i).updateConsole(console);
                }
            }
        }
    }
*/
}