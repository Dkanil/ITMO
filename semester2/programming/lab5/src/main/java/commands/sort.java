package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для сортировки коллекции в естественном порядке.
 */
public class sort extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды sort.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public sort(Console console, CollectionManager collectionManager) {
        super("sort", "отсортировать коллекцию в естественном порядке");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сортировки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionStatus(false, "У команды sort не должно быть аргументов!\nПример корректного ввода: " + getName());
            }
            collectionManager.sort();
            return new ExecutionStatus(true, "Коллекция успешно отсортирована!");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при сортировке коллекции!");
        }
    }
}