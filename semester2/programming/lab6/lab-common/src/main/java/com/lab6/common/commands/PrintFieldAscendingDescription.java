package com.lab6.common.commands;

import com.lab6.common.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.managers.CollectionManager;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке возрастания.
 */
public class PrintFieldAscendingDescription extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды printFieldAscendingDescription.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public PrintFieldAscendingDescription(Console console, CollectionManager collectionManager) {
        super(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getDescription(), console, new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значений поля description всех элементов в порядке возрастания.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        if (collectionManager.getBands().isEmpty()) {
            return new ExecutionStatus(false, "Коллекция пуста!");
        }
        collectionManager.sort();
        for (var band : collectionManager.getBands()) {
            console.println(band.getDescription());
        }
        return new ExecutionStatus(true, "Операция выполнена!");
    }
}
