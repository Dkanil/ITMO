package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для вывода всех элементов коллекции в строковом представлении.
 */
public class show extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды show.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public show(Console console, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода всех элементов коллекции.
     * @param arg Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String arg) {
        try {
            if (!arg.isEmpty()) {
                return new ExecutionStatus(false, "У команды show не должно быть аргументов!\nПример корректного ввода: " + getName());
            }
            console.println("Вывод всех элементов коллекции:");
            collectionManager.getCollection().forEach(band -> console.println(band.toString()));
            if (collectionManager.getCollection().isEmpty()) {
                console.println("Коллекция пуста.");
            }
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выводе элементов коллекции!");
        }
        return new ExecutionStatus(true, "Вывод всех элементов коллекции успешно завершен!");
    }
}