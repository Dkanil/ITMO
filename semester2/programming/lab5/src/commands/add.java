package commands;

import models.MusicBand;
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
                return new ExecutionStatus(false, "У команды add должен быть только один аргумент!\nПример корректного ввода: " + getName());
            }
            console.println("Добавление элемента в коллекцию...");
            MusicBand band = Asker.askBand(console, collectionManager.getFreeId());

            if (band != null && band.validate()){
                collectionManager.add(band);
            }
            else {
                return new ExecutionStatus(false, "Введены некорректные данные!");
            }
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при добавлении элемента в коллекцию!");
        }

        return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
    }
}
