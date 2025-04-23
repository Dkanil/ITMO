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
    CollectionManager collectionManager;

    /**
     * Конструктор команды save.
     * @param collectionManager Менеджер коллекции.
     */
    public Save(CollectionManager collectionManager) {
        super(CommandNames.SAVE.getName(), CommandNames.SAVE.getDescription(), new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сохранения коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        collectionManager.saveCollection();
        return new ExecutionStatus(true, "Collection successfully saved!");
    }
}
