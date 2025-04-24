package com.lab6.server.commands;

import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для сохранения коллекции в файл.
 */
public class Save extends Command<EmptyValidator> {
    CollectionManager collectionManager = CollectionManager.getInstance();
    /**
     * Конструктор команды save.
     */
    public Save() {
        super(CommandNames.SAVE.getName(), CommandNames.SAVE.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет команду сохранения коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        return collectionManager.saveCollection();
    }
}
