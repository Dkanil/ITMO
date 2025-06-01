package com.lab8.server.commands;

import com.lab8.common.utility.Pair;
import com.lab8.server.utility.Command;
import com.lab8.server.utility.CommandNames;
import com.lab8.common.validators.EmptyValidator;
import com.lab8.common.utility.ExecutionStatus;

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
     *
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        return collectionManager.clear(user);
    }
}
