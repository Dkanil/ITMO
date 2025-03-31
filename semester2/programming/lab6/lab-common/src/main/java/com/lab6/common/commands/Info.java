package com.lab6.common.commands;

import com.lab6.common.commands.validators.EmptyValidator;
import com.lab6.common.managers.CollectionManager;
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
        console.println("Тип коллекции: " + collectionManager.getBands().getClass().getName());
        console.println("Дата инициализации: " + collectionManager.getInitializationDate());
        console.println("Дата последнего сохранения: " + collectionManager.getLastSaveDate());
        console.println("Количество элементов: " + collectionManager.getBands().size());
        return new ExecutionStatus(true, "Информация о коллекции успешно выведена!");
    }
}
