package managers;

import utility.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Executer {
    private Console console;
    private int scriptStackCounter = 0;

    public Executer(Console console) {
        this.console = console;
    }

    private ExecutionStatus runCommand(String[] userCommand) {
        try {
            console.println("Выполнение команды '" + userCommand[0] + "'");
            if (userCommand[0].equals("execute_script")) {
                return runScript(userCommand);
            }
            var command = CommandManager.getCommand(userCommand[0]);
            if (command == null) {
                return new ExecutionStatus(false, "Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'.");
            }
            else {
                return command.run(userCommand[1]);
            }
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выполнении команды!");
        }
    }

    public ExecutionStatus runScript(String[] args) {
        try {
            scriptStackCounter++;
            if (scriptStackCounter > 100) {
                console.printError("Превышена максимальная глубина рекурсии!");
                scriptStackCounter--;
                return new ExecutionStatus(false, "Превышена максимальная глубина рекурсии!");
            }
            if (args.length != 2) {
                scriptStackCounter--;
                return new ExecutionStatus(false, "У команды execute_script должен быть только один аргумент!\nПример корректного ввода: " + "execute_script file_name");
            }
            String fileName = args[1];
            console.println("Выполнение скрипта '" + fileName + "'");

            try (BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) { //todo здесь ошибка возникает

                while (scriptStackCounter > 0) {
                    if (!input.readLine().equals("exit")){
                        String[] inputCommand = (input.readLine().trim() + " ").split(" ", 2);
                        inputCommand[1] = inputCommand[1].trim();
                        runCommand(inputCommand);
                    }
                    else {
                        scriptStackCounter--;
                        return new ExecutionStatus(true, "Скрипт успешно выполнен.");
                    }
                }
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
            //String[] inputCommand = {"", ""};
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