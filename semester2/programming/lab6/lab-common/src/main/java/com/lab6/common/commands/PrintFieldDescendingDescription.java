package com.lab6.common.commands;

import com.lab6.common.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.managers.CollectionManager;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке убывания.
 */
public class PrintFieldDescendingDescription extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды printFieldDescendingDescription.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public PrintFieldDescendingDescription(Console console, CollectionManager collectionManager) {
        super(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getDescription(), console, new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значений поля description всех элементов в порядке убывания.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        if (collectionManager.getBands().isEmpty()) {
            return new ExecutionStatus(false, "Коллекция пуста!");
        }
        collectionManager.sort();
        for (int i = collectionManager.getBands().size() - 1; i >= 0; i--) {
            console.println(collectionManager.getBands().get(i).getDescription());
        }
        return new ExecutionStatus(true, "Операция выполнена!");
    }
}
