package commands;

import commands.validators.EmptyValidator;
import utility.*;
import managers.Executer;

/**
 * Класс команды для выполнения скрипта из указанного файла.
 */
public class ExecuteScript extends Command<EmptyValidator> {

    /**
     * Конструктор команды executeScript.
     *
     * @param console Консоль для ввода/вывода.
     */
    public ExecuteScript(Console console) {
        super(CommandNames.EXECUTE_SCRIPT.getName() + " file_name", CommandNames.EXECUTE_SCRIPT.getDescription(), console, null);
    }

    /**
     * Заглушка для команды выполнения скрипта. Реализация в {@link Executer}.
     *
     * @param arg Имя файла скрипта.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus runInternal(String arg) {
        return null;
    }
}