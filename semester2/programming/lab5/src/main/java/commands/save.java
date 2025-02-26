package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для сохранения коллекции в файл.
 */
public class save extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды save.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public save(utility.Console console, managers.CollectionManager collectionManager) {
        super("save", "сохранить коллекцию в файл");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сохранения коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionStatus(false, "У команды save не должно быть аргументов!\nПример корректного ввода: " + getName());
            }
            collectionManager.saveCollection();
            return new ExecutionStatus(true, "Коллекция успешно сохранена!");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при сохранении коллекции!");
        }
    }
}