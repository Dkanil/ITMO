package com.lab6.server.commands;

import com.lab6.common.utility.Command;
import com.lab6.common.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для очистки коллекции.
 */
public class Clear extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды clear.
     * @param collectionManager Менеджер коллекции.
     */
    public Clear(CollectionManager collectionManager) {
        super(CommandNames.CLEAR.getName(), CommandNames.CLEAR.getDescription(), new EmptyValidator());
        this.collectionManager = collectionManager;
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
