package commands;

import utility.*;
import managers.*;

public class add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public add(Console console, CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public ExecutionStatus apply(String[] args) {
        try {
            if (args.length != 1) {
                return new ExecutionStatus(false, "У команды add должен быть только один аргумент!\nПример: " + getName());
            }
            console.println("Добавление элемента в коллекцию...");
            collectionManager.add(args[0]);
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при добавлении элемента в коллекцию!");
        }

        return new ExecutionStatus(true, "Команда add работает!");
    }
}
