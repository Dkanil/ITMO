package com.lab8.server.commands;

import com.lab8.common.utility.Pair;
import com.lab8.server.utility.Command;
import com.lab8.server.utility.CommandNames;
import com.lab8.common.validators.EmptyValidator;
import com.lab8.common.utility.ExecutionStatus;
/**
 * Класс команды для вывода всех элементов коллекции в строковом представлении.
 */
public class Show extends Command<EmptyValidator> {

    /**
     * Конструктор команды show.
     */
    public Show() {
        super(CommandNames.SHOW.getName(), CommandNames.SHOW.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет команду вывода всех элементов коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        if (collectionManager.getCollection().isEmpty()) {
            return new ExecutionStatus(true, "Коллекция пуста.\n");
        }
        return new ExecutionStatus(true, collectionManager.getCollection());
    }
}