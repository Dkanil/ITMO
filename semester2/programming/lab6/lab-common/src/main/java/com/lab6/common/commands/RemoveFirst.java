package com.lab6.common.commands;

import com.lab6.common.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.managers.CollectionManager;

/**
 * Класс команды для удаления первого элемента из коллекции.
 */
public class RemoveFirst extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeFirst.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveFirst(Console console, CollectionManager collectionManager) {
        super(CommandNames.REMOVE_FIRST.getName(), CommandNames.REMOVE_FIRST.getDescription(), console, new EmptyValidator());
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
