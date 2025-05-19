package com.lab7.server.commands;

import com.lab7.common.utility.Pair;
import com.lab7.server.utility.Command;
import com.lab7.server.utility.CommandNames;
import com.lab7.common.validators.EmptyValidator;
import com.lab7.common.utility.ExecutionStatus;

/**
 * Класс команды для сортировки коллекции в естественном порядке.
 */
public class Sort extends Command<EmptyValidator> {

    /**
     * Конструктор команды sort.
     */
    public Sort() {
        super(CommandNames.SORT.getName(), CommandNames.SORT.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет команду сортировки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        collectionManager.sort();
        return new ExecutionStatus(true, "Коллекция успешно отсортирована!");
    }
}
