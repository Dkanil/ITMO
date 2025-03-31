package com.lab6.common.commands;

import com.lab6.common.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.managers.CollectionManager;

/**
 * Класс команды для сортировки коллекции в естественном порядке.
 */
public class Sort extends Command<EmptyValidator> {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды sort.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Sort(Console console, CollectionManager collectionManager) {
        super(CommandNames.SORT.getName(), CommandNames.SORT.getDescription(), console, new EmptyValidator());
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
