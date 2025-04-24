package com.lab6.server.commands;

import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке убывания.
 */
public class PrintFieldDescendingDescription extends Command<EmptyValidator> {

    /**
     * Конструктор команды printFieldDescendingDescription.
     */
    public PrintFieldDescendingDescription() {
        super(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет команду вывода значений поля description всех элементов в порядке убывания.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        if (collectionManager.getBands().isEmpty()) {
            return new ExecutionStatus(true, "Коллекция пуста!");
        }
        collectionManager.sort();
        StringBuilder helpMessage = new StringBuilder();
        for (int i = collectionManager.getBands().size() - 1; i >= 0; i--) {
            helpMessage.append(collectionManager.getBands().get(i).getDescription()).append("\n");
        }
        helpMessage.append("Значения поля description всех элементов в порядке убывания успешно выведены!");
        return new ExecutionStatus(true, helpMessage.toString());
    }
}
