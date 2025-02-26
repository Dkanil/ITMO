package commands.emptyArgumentCommands;

import commands.CommandNames;
import utility.*;
import managers.*;

/**
 * Класс команды для удаления первого элемента из коллекции.
 */
public class RemoveFirst extends NoArgumentCommand {
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeFirst.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveFirst(utility.Console console, managers.CollectionManager collectionManager) {
        super(CommandNames.REMOVE_FIRST.getName(), CommandNames.REMOVE_FIRST.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления первого элемента коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            if (collectionManager.getCollection().isEmpty()) {
                return new ExecutionStatus(false, "Коллекция пуста!");
            }
            collectionManager.removeFirst();
            return new ExecutionStatus(true, "Первый элемент успешно удален!");
        } else {
            return ArgumentStatus;
        }
    }
}