package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для удаления элемента из коллекции по его id.
 */
public class removeById extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeById.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public removeById(utility.Console console, managers.CollectionManager collectionManager) {
        super("remove_by_id id", "удалить элемент из коллекции по его id");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления элемента коллекции по его id.
     * @param argument Аргумент команды, содержащий id элемента.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        if (argument.isEmpty()) {
            return new ExecutionStatus(false, "У команды remove_by_id должен быть аргумент (id элемента коллекции)!");
        }
        try {
            Long id = Long.parseLong(argument);
            if (collectionManager.getById(id) == null) {
                return new ExecutionStatus(false, "Элемент с указанным id не найден!");
            }
            collectionManager.removeById(id);
            return new ExecutionStatus(true, "Элемент успешно удален!");
        } catch (NumberFormatException e) {
            return new ExecutionStatus(false, "Формат аргумента неверен! Он должен быть целым числом.");
        }
    }
}