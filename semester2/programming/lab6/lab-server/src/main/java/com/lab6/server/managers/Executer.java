package com.lab6.server.managers;

import com.lab6.common.managers.CommandManager;
import com.lab6.common.models.MusicBand;
import com.lab6.common.utility.Command;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.validators.ArgumentValidator;
import com.lab6.common.utility.AskingCommand;

/**
 * Класс, выполняющий команды и скрипты.
 */
public class Executer {
    private final Console console;
    private final CommandManager commandManager;

    /**
     * Конструктор для создания объекта Executer.
     * @param console консоль для ввода и вывода данных
     */
    public Executer(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }
    //todo убрать дубль кода
    public ExecutionStatus validateCommand(String[] userCommand) {
        try {
            if (userCommand[0].equals("execute_script")) {
                return new ExecutionStatus(true, "Введена команда 'execute_script'. Валидация аргументов не требуется.");
            }
            Command<?> command = commandManager.getCommand(userCommand[0]);
            if (command == null) {
                return new ExecutionStatus(false, "Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'.");
            } else {
                ArgumentValidator argumentValidator = command.getArgumentValidator();
                return argumentValidator.validate(userCommand[1], command.getName());
            }
        } catch (NullPointerException e) {
            return new ExecutionStatus(false, "Введено недостаточно аргументов для выполнения последней команды!");
        }
        catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выполнении команды!");
        }
    }

    /**
     * Выполняет команду.
     * @param userCommand массив строк, представляющий команду
     * @return статус выполнения команды
     */
    public ExecutionStatus runCommand(String[] userCommand, MusicBand musicBand) {
        try {
            ExecutionStatus validateStatus = validateCommand(userCommand);
            if (validateStatus.isSuccess()) {
//                if (userCommand[0].equals("execute_script")) { //todo проверить
//                    return runScript(userCommand[1]);
//                }
                var command = commandManager.getCommand(userCommand[0]);
                if (command == null) {
                    return new ExecutionStatus(false, "Команда '" + userCommand[0] + "' не найдена! Для показа списка команд введите 'help'.");
                } else {
                    console.println("Выполнение команды '" + userCommand[0] + "'");
                    if (AskingCommand.class.isAssignableFrom(command.getClass())) {
                        return ((AskingCommand<?>) command).run(userCommand[1], musicBand); // todo если команда из скрипта то нужно исправить
                    } else {
                        return command.run(userCommand[1]);
                    }
                }
            } else {
                return new ExecutionStatus(false, validateStatus.getMessage());
            }
        } catch (NullPointerException e) {
            return new ExecutionStatus(false, "Введено недостаточно аргументов для выполнения последней команды!");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выполнении команды!");
        }
    }
}
