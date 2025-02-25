package managers;

import utility.*;
import utility.Console;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

public class Executer {
    private Console console;
    private int scriptStackCounter = 0;

    public Executer(Console console) {
        this.console = console;
    }


    private ExecutionStatus runCommand(String[] userCommand) {
        try {
            if (userCommand[0].equals("execute_script")) {
                return runScript(userCommand[1]);
            }
            var command = CommandManager.getCommand(userCommand[0]);
            if (command == null) {
                return new ExecutionStatus(false, "Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'.");
            }
            else {
                console.println("Выполнение команды '" + userCommand[0] + "'");
                return command.run(userCommand[1]);
            }
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выполнении команды!");
        }
    }

    public ExecutionStatus runScript(String fileName) {
        try {
            scriptStackCounter++;
            if (scriptStackCounter > 100) {
                scriptStackCounter--;
                return new ExecutionStatus(false, "Превышена максимальная глубина рекурсии!");
            }
            if (fileName.length() == 0) {
                scriptStackCounter--;
                return new ExecutionStatus(false, "У команды execute_script должен быть только один аргумент!\nПример корректного ввода: " + "execute_script file_name");
            }
            console.println("Запуск скрипта '" + fileName + "'");
            try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) { //todo здесь ошибка возникает

                while (scriptStackCounter > 0) {
                    String line = input.readLine();
                    if (!line.equals("exit")){
                        String[] inputCommand = (line.trim() + " ").split(" ", 2);
                        inputCommand[1] = inputCommand[1].trim();
                        ExecutionStatus commandStatus = runCommand(inputCommand);
                        if (commandStatus.isSuccess()) {
                            console.println(commandStatus.getMessage());
                        }
                        else {
                            console.printError(commandStatus.getMessage());
                            return new ExecutionStatus(false, "Выполнение скрипта приостановлено.");
                        }
                    }
                    else {
                        scriptStackCounter--;
                        return new ExecutionStatus(true, "Скрипт успешно выполнен.");
                    }
                }
            } catch (FileNotFoundException e) {
                return new ExecutionStatus(false, "Не удаётся найти файл скрипта!");
            } catch (Exception e) {
                return new ExecutionStatus(false, "Произошла ошибка при выполнении команды скрипта!");
            }
            return new ExecutionStatus(true, "");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при запуске скрипта!");
        }
    }

    public void interactiveMode() {
        try {
            while (true) {
                String[] inputCommand = (console.readln().trim() + " ").split(" ", 2);
                inputCommand[1] = inputCommand[1].trim();
                ExecutionStatus commandStatus = runCommand(inputCommand);
                if (commandStatus.isSuccess()) {
                    console.println(commandStatus.getMessage());
                }
                else {
                    console.printError(commandStatus.getMessage());
                }
            }
        } catch (Exception e) {
            console.printError("Произошла ошибка при выполнении команды!");
        }
    }
}