package com.lab6.server.commands;

import com.lab6.server.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для вывода всех элементов коллекции в строковом представлении.
 */
public class Show extends Command<EmptyValidator> {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды show.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Show(Console console, CollectionManager collectionManager) {
        super(CommandNames.SHOW.getName(), CommandNames.SHOW.getDescription(), console, new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода всех элементов коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        StringBuilder helpMessage = new StringBuilder("Вывод всех элементов коллекции:\n");
        if (collectionManager.getCollection().isEmpty()) {
            helpMessage.append("Коллекция пуста.\n");
        }
        collectionManager.getCollection().forEach(band -> helpMessage.append(band.toString()).append("\n"));
        helpMessage.append("Вывод всех элементов коллекции успешно завершен!");
        return new ExecutionStatus(true, helpMessage.toString());
    }
}
