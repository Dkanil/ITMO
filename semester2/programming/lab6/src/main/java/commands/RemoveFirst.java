package commands;

import commands.validators.EmptyValidator;
import utility.*;
import managers.*;

/**
 * Класс команды для удаления первого элемента из коллекции.
 */
public class RemoveFirst extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeFirst.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveFirst(utility.Console console, managers.CollectionManager collectionManager) {
        super(CommandNames.REMOVE_FIRST.getName(), CommandNames.REMOVE_FIRST.getDescription(), console, new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления первого элемента коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        if (collectionManager.getCollection().isEmpty()) {
            return new ExecutionStatus(false, "Коллекция пуста!");
        }
        collectionManager.removeFirst();
        return new ExecutionStatus(true, "Первый элемент успешно удален!");
    }
}