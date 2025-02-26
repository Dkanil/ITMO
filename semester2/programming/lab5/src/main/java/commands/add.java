package commands;

import models.MusicBand;
import utility.*;
import managers.*;

/**
 * Класс команды для добавления нового элемента в коллекцию.
 */
public class add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    /**
     * Конструктор команды add.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public add(Console console, CollectionManager collectionManager) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления нового элемента в коллекцию.
     * @param arg Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String arg) {
        try {
            if (!arg.isEmpty()) {
                return new ExecutionStatus(false, "У команды add ввод аргументов построчный!\nПример корректного ввода: " + getName());
            }
            console.println("Добавление элемента в коллекцию...");
            MusicBand band = Asker.askBand(console, collectionManager.getFreeId());

            if (band != null && band.validate()){
                collectionManager.add(band);
            } else {
                return new ExecutionStatus(false, "Введены некорректные данные!");
            }
        } catch (Asker.Breaker e) {
            return new ExecutionStatus(false, "Ввод был прерван пользователем!");
        }
        return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
    }
}