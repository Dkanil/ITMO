package com.lab6.common.commands;

import com.lab6.common.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.managers.CollectionManager;

/**
 * Класс команды для очистки коллекции.
 */
public class Clear extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды clear.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Clear(Console console, CollectionManager collectionManager) {
        super(CommandNames.CLEAR.getName(), CommandNames.CLEAR.getDescription(), console, new EmptyValidator());
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
