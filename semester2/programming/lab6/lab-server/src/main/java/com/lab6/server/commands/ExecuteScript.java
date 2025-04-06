package com.lab6.server.commands;

import com.lab6.server.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.Executer;

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
