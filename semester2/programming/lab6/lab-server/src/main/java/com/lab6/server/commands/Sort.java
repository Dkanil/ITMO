package com.lab6.server.commands;

import com.lab6.common.utility.Command;
import com.lab6.common.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для сортировки коллекции в естественном порядке.
 */
public class Sort extends Command<EmptyValidator> {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды sort.
     * @param collectionManager Менеджер коллекции.
     */
    public Sort(CollectionManager collectionManager) {
        super(CommandNames.SORT.getName(), CommandNames.SORT.getDescription(), new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сортировки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        collectionManager.sort();
        return new ExecutionStatus(true, "Коллекция успешно отсортирована!");
    }
}
