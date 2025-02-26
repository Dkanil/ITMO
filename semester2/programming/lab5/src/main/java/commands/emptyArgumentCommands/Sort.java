package commands.emptyArgumentCommands;

import commands.CommandNames;
import utility.*;
import managers.*;

/**
 * Класс команды для сортировки коллекции в естественном порядке.
 */
public class Sort extends NoArgumentCommand {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды sort.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Sort(Console console, CollectionManager collectionManager) {
        super(CommandNames.SORT.getName(), CommandNames.SORT.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сортировки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            collectionManager.sort();
            return new ExecutionStatus(true, "Коллекция успешно отсортирована!");
        } else {
            return ArgumentStatus;
        }
    }
}