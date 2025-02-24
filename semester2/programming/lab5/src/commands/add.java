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
    public ExecutionStatus run(String arg) {
        try {
            if (arg.length() != 0) {
                return new ExecutionStatus(false, "У команды add ввод аргументов построчный!\nПример корректного ввода: " + getName());
            }
            console.println("Добавление элемента в коллекцию...");
            MusicBand band = Asker.askBand(console, collectionManager.getFreeId());

            if (band != null && band.validate()){
                collectionManager.add(band);
            }
            else {
                return new ExecutionStatus(false, "Введены некорректные данные!");
            }
        } catch (Asker.Breaker e) {
            return new ExecutionStatus(false, "Ввод был прерван пользователем!");
        }
        try {
            collectionManager.saveCollection();
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при сохранении коллекции!");
        }
        return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
    }
}
