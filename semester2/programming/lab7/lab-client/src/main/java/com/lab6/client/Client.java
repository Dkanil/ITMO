package com.lab6.client;

import com.lab6.client.managers.NetworkManager;
import com.lab6.client.utility.ElementValidator;
import com.lab6.client.utility.FileConsole;
import com.lab6.common.models.MusicBand;
import com.lab6.client.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Pair;
import com.lab6.common.utility.Request;
import com.lab6.common.utility.Response;
import com.lab6.client.utility.StandartConsole;
import com.lab6.common.validators.ArgumentValidator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.Map;

//Вариант 7995
public final class Client {
    private static final Console console = new StandartConsole();
    private static final int SERVER_PORT = 13876;
    private static final String SERVER_HOST = "localhost";
    private static Map<String, Pair<ArgumentValidator, Boolean>> commandsData;
    private static final NetworkManager networkManager = new NetworkManager(SERVER_PORT, SERVER_HOST);
    private static int scriptStackCounter = 0;
    private static int attempts = 1;

    public static void main(String[] args) {
        do {
            try {
                networkManager.connect();
                commandsData = networkManager.receive().getCommandsMap();
                attempts = 1;
                console.println("Connected to " + SERVER_HOST + ":" + SERVER_PORT);
                while (true) {
                    console.println("Введите команду:");
                    String inputCommand = console.readln();
                    ExecutionStatus argumentStatus = validateCommand((inputCommand.trim() + " ").split(" ", 2));
                    if (!argumentStatus.isSuccess()) {
                        console.printError(argumentStatus.getMessage());
                    }
                    else {
                        Request request = prepareRequest(console, inputCommand);
                        if (request == null) {
                            continue; // Прерываем выполнение команды, если клиент не ввёл элемент коллекции
                        }

                        networkManager.send(request);
                        Response response = networkManager.receive();
                        if (response.getExecutionStatus().isSuccess()) {
                            if (response.getExecutionStatus().getMessage() == null) {
                                response.getExecutionStatus().getCollection().forEach(item -> console.println(item.toString()));
                            }
                            else {
                                console.println(response.getExecutionStatus().getMessage());
                            }
                        } else {
                            console.printError(response.getExecutionStatus().getMessage());
                        }
                    }
                }
            } catch (BufferOverflowException | BufferUnderflowException | IOException e) {
                console.printError("Не удалось подключиться к серверу. Проверьте, запущен ли сервер и доступен ли он по адресу " + SERVER_HOST + ":" + SERVER_PORT);
                try {
                    Thread.sleep(2000);
                    attempts++;
                } catch (InterruptedException ignored) {}
            } catch (ClassNotFoundException e) {
                console.printError("Ошибка при работе с сервером: " + e.getMessage());
            }
        } while (attempts <= 5);
        console.printError("Превышено максимальное количество попыток подключения к серверу.");
    }

    private static Request askingRequest(Console console, String inputCommand) {
        ElementValidator elementValidator = new ElementValidator();
        Pair<ExecutionStatus, MusicBand> validationStatusPair = elementValidator.validateAsking(console, 1L); //На клиенте вводится id=1L, на сервере он меняется на корректный
        if (!validationStatusPair.getFirst().isSuccess()) {
            console.printError(validationStatusPair.getFirst().getMessage());
            return null;
        } else {
            return new Request(inputCommand, validationStatusPair.getSecond());
        }
    }

    private static ExecutionStatus validateCommand(String[] userCommand) {
        try {
            if (userCommand[0].equals("exit")) {
                console.println("Завершение работы клиента...");
                try {
                    networkManager.close();
                } catch (IOException e) {
                    console.printError("Не удалось закрыть соединение с сервером.");
                }
                System.exit(0);
                return null;
            } else if (userCommand[0].equals("execute_script")) {
                return new ExecutionStatus(true, "Введена команда 'execute_script'. Валидация аргументов не требуется.");
            } else {
                var argumentValidator = commandsData.get(userCommand[0]);
                if (argumentValidator == null) {
                    return new ExecutionStatus(false, "Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'.");
                } else {
                    return argumentValidator.getFirst().validate(userCommand[1].trim(), userCommand[0]);
                }
            }
        } catch (NullPointerException e) {
            return new ExecutionStatus(false, "Введено недостаточно аргументов для выполнения последней команды!");
        }
    }

    private static Request prepareRequest(Console console, String inputCommand) {
        String[] commands = (inputCommand.trim() + " ").split(" ", 2);
        if (commandsData.get(commands[0]).getSecond()) {
            return askingRequest(console, inputCommand); // Если команда требует построчного ввода
        } else if (commands[0].equals("execute_script")) {
            ExecutionStatus scriptStatus = runScript(commands[1].trim());
            if (!scriptStatus.isSuccess()) {
                console.printError(scriptStatus.getMessage());
                return null;
            }
            return null;
        } else {
            return new Request(inputCommand);
        }
    }

    private static ExecutionStatus runScript(String fileName) {
        try {
            scriptStackCounter++;
            if (scriptStackCounter > 100) {
                scriptStackCounter--;
                return new ExecutionStatus(false, "Превышена максимальная глубина рекурсии!");
            }
            if (fileName.isEmpty()) {
                scriptStackCounter--;
                return new ExecutionStatus(false, "У команды execute_script должен быть ровно один аргумент!\nПример корректного ввода: execute_script file_name");
            }
            console.println("Запуск скрипта '" + fileName + "'");
            try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
                Console FileConsole = new FileConsole(input);
                while (scriptStackCounter > 0) {
                    String line = input.readLine();
                    if (!line.equals("exit")) {

                        Request request = prepareRequest(FileConsole, line);
                        if (request == null) {
                            return new ExecutionStatus(false, "Выполнение скрипта остановлено");
                        }
                        networkManager.send(request);
                        Response response = networkManager.receive();
                        ExecutionStatus commandStatus = response.getExecutionStatus();

                        if (response.getExecutionStatus().isSuccess()) {
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
    }
}