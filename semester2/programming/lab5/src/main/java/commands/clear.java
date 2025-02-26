package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для очистки коллекции.
 */
public class clear extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды clear.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public clear(Console console, CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду очистки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionStatus(false, "У команды clear не должно быть аргументов!\nПример корректного ввода: " + getName());
            }
            collectionManager.clear();
            return new ExecutionStatus(true, "Коллекция успешно очищена!");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при очистке коллекции!");
        }
    }
}