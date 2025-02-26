package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для удаления первого элемента из коллекции.
 */
public class removeFirst extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeFirst.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public removeFirst(utility.Console console, managers.CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления первого элемента коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionStatus(false, "У команды remove_first не должно быть аргументов!\nПример корректного ввода: " + getName());
            }
            if (collectionManager.getCollection().isEmpty()) {
                return new ExecutionStatus(false, "Коллекция пуста!");
            }
            collectionManager.removeFirst();
            return new ExecutionStatus(true, "Первый элемент успешно удален!");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при удалении первого элемента коллекции!");
        }
    }
}