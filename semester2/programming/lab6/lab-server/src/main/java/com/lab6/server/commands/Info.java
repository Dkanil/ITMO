package com.lab6.server.commands;

import com.lab6.server.commands.validators.EmptyValidator;
import com.lab6.server.managers.CollectionManager;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс команды для вывода информации о коллекции.
 */
public class Info extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды info.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Info(Console console, CollectionManager collectionManager) {
        super(CommandNames.INFO.getName(), CommandNames.INFO.getDescription(), console, new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода информации о коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        String infoMessage = "Тип коллекции: " + collectionManager.getBands().getClass().getName() +
                "\nДата инициализации: " + collectionManager.getInitializationDate() +
                "\nДата последнего сохранения: " + collectionManager.getLastSaveDate() +
                "\nКоличество элементов: " + collectionManager.getBands().size() +
                "\nИнформация о коллекции успешно выведена!";
        return new ExecutionStatus(true, infoMessage);
    }
}
