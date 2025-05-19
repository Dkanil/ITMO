package com.lab7.server.commands;

import com.lab7.common.utility.Pair;
import com.lab7.server.utility.Command;
import com.lab7.server.utility.CommandNames;
import com.lab7.common.validators.EmptyValidator;
import com.lab7.common.utility.ExecutionStatus;

/**
 * Класс команды для вывода информации о коллекции.
 */
public class Info extends Command<EmptyValidator> {

    /**
     * Конструктор команды info.
     */
    public Info() {
        super(CommandNames.INFO.getName(), CommandNames.INFO.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет команду вывода информации о коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        String infoMessage = "Тип коллекции: " + collectionManager.getBands().getClass().getName() +
                "\nДата инициализации: " + collectionManager.getInitializationDate() +
                "\nДата последнего сохранения: " + collectionManager.getLastSaveDate() +
                "\nКоличество элементов: " + collectionManager.getBands().size() +
                "\nИнформация о коллекции успешно выведена!";
        return new ExecutionStatus(true, infoMessage);
    }
}
