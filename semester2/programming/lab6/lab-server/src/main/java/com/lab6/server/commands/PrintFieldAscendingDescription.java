package com.lab6.server.commands;

import com.lab6.common.utility.Command;
import com.lab6.common.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке возрастания.
 */
public class PrintFieldAscendingDescription extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды printFieldAscendingDescription.
     * @param collectionManager Менеджер коллекции.
     */
    public PrintFieldAscendingDescription(CollectionManager collectionManager) {
        super(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getDescription(), new EmptyValidator());
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
        StringBuilder helpMessage = new StringBuilder();
        for (var band : collectionManager.getBands()) {
            helpMessage.append(band.getDescription()).append("\n");
        }
        helpMessage.append("Значения поля description всех элементов в порядке возрастания успешно выведены!");
        return new ExecutionStatus(true, helpMessage.toString());
    }
}
