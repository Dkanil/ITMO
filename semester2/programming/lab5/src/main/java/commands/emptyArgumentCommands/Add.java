package commands.emptyArgumentCommands;

import commands.*;
import utility.*;
import managers.*;

/**
 * Класс команды для добавления нового элемента в коллекцию.
 */
public class Add extends NoArgumentCommand implements Asking {
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды add.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Add(Console console, CollectionManager collectionManager) {
        super(CommandNames.ADD.getName() + " {element}", CommandNames.ADD.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления нового элемента в коллекцию.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            console.println("Добавление элемента в коллекцию...");
            Asking.Pair validationStatusPair = validate(console, collectionManager.getFreeId());
            if (validationStatusPair.getExecutionStatus().isSuccess()) {
                collectionManager.add(validationStatusPair.getBand());
            }
            return validationStatusPair.getExecutionStatus();
        } else {
            return ArgumentStatus;
        }
    }
}