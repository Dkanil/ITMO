package com.lab6.server.commands;

import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс команды для выполнения скрипта из указанного файла.
 */
public class ExecuteScript extends Command<EmptyValidator> {

    /**
     * Конструктор команды executeScript.
     */
    public ExecuteScript() {
        super(CommandNames.EXECUTE_SCRIPT.getName() + " file_name", CommandNames.EXECUTE_SCRIPT.getDescription(), null);
    }

    /**
     * Заглушка для команды выполнения скрипта. Реализация в Executer.
     *
     * @param arg Имя файла скрипта.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus runInternal(String arg) {
        return new ExecutionStatus(true, "Данную команду нельзя отправить на сервер");
    }
}
