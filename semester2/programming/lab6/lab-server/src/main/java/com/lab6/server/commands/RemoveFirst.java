package com.lab6.server.commands;

import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для удаления первого элемента из коллекции.
 */
public class RemoveFirst extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeFirst.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveFirst(CollectionManager collectionManager) {
        super(CommandNames.REMOVE_FIRST.getName(), CommandNames.REMOVE_FIRST.getDescription(), new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления первого элемента коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        if (collectionManager.getCollection().isEmpty()) {
            return new ExecutionStatus(false, "Коллекция пуста!");
        }
        collectionManager.removeFirst();
        return new ExecutionStatus(true, "Первый элемент успешно удален!");
    }
}
