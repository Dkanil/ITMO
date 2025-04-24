package com.lab6.server.commands;

import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс команды для очистки коллекции.
 */
public class Clear extends Command<EmptyValidator> {

    /**
     * Конструктор команды clear.
     */
    public Clear() {
        super(CommandNames.CLEAR.getName(), CommandNames.CLEAR.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет команду очистки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        collectionManager.clear();
        return new ExecutionStatus(true, "Коллекция успешно очищена!");
    }
}
