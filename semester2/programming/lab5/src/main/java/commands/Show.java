package commands;

import commands.validators.EmptyValidator;
import utility.*;
import managers.*;

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
        console.println("Вывод всех элементов коллекции:");
        if (collectionManager.getCollection().isEmpty()) {
            console.println("Коллекция пуста.");
        }
        collectionManager.getCollection().forEach(band -> console.println(band.toString()));
        return new ExecutionStatus(true, "Вывод всех элементов коллекции успешно завершен!");
    }
}