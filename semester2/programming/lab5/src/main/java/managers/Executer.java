package managers;

import commands.CommandNames;
import utility.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

/**
 * Класс, выполняющий команды и скрипты.
 */
public class Executer {
    private Console console;
    private int scriptStackCounter = 0;

    /**
     * Конструктор для создания объекта Executer.
     * @param console консоль для ввода и вывода данных
     */
    public Executer(Console console) {
        this.console = console;
    }

    /**
     * Выполняет команду.
     * @param userCommand массив строк, представляющий команду
     * @return статус выполнения команды
     */
    private ExecutionStatus runCommand(String[] userCommand) {
        try {
            if (userCommand[0].equals("execute_script")) {
                return runScript(userCommand[1]);
            }
            var command = CommandManager.getCommand(userCommand[0]);
            if (command == null) {
                return new ExecutionStatus(false, "Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'.");
            } else {
                console.println("Выполнение команды '" + userCommand[0] + "'");
                return command.run(userCommand[1]);
            }
        } catch (NullPointerException e) {
            return new ExecutionStatus(false, "Введено недостаточно аргументов для выполнения последней команды!");
        }
        catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выполнении команды!");
        }
    }

    /**
     * Выполняет скрипт из файла.
     * @param fileName имя файла скрипта
     * @return статус выполнения скрипта
     */
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
                // Костыльно подменяем консоль для команд, которые требуют построчного ввода пользователя
                CommandManager.getCommand(CommandNames.ADD.getName()).updateConsole(FileConsole);
                CommandManager.getCommand(CommandNames.ADD_IF_MIN.getName()).updateConsole(FileConsole);
                CommandManager.getCommand(CommandNames.UPDATE.getName()).updateConsole(FileConsole);
                while (scriptStackCounter > 0) {
                    String line = input.readLine();
                    if (!line.equals("exit")) {
                        String[] inputCommand = (line.trim() + " ").split(" ", 2);
                        inputCommand[1] = inputCommand[1].trim();
                        ExecutionStatus commandStatus = runCommand(inputCommand);
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
            CommandManager.getCommand(CommandNames.ADD.getName()).updateConsole(console);
            CommandManager.getCommand(CommandNames.ADD_IF_MIN.getName()).updateConsole(console);
            CommandManager.getCommand(CommandNames.UPDATE.getName()).updateConsole(console);
        }
    }

    /**
     * Запускает интерактивный режим.
     */
    public void interactiveMode() {
        try {
            while (true) {
                String[] inputCommand = (console.readln().trim() + " ").split(" ", 2);
                inputCommand[1] = inputCommand[1].trim();
                ExecutionStatus commandStatus = runCommand(inputCommand);
                if (commandStatus.isSuccess()) {
                    console.println(commandStatus.getMessage());
                } else {
                    console.printError(commandStatus.getMessage());
                }
            }
        } catch (Exception e) {
            console.printError("Произошла ошибка при выполнении команды!");
        }
    }
}