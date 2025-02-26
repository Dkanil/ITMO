package commands;

import models.MusicBand;
import utility.*;
import managers.*;

import java.util.Comparator;
import java.util.Stack;

/**
 * Класс команды для добавления нового элемента в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
 */
public class addIfMin extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды addIfMin.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public addIfMin(Console console, CollectionManager collectionManager) {
        super("add_if_min {element}", "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления нового элемента в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionStatus(false, "У команды add_if_min ввод аргументов построчный!\nПример корректного ввода: " + getName());
            }

            console.println("Добавление элемента в коллекцию...");
            MusicBand band = Asker.askBand(console, collectionManager.getFreeId());
            if (band != null && band.validate()) {
                if (collectionManager.getCollection().isEmpty()) {
                    collectionManager.add(band);
                    return new ExecutionStatus(true, "Коллекция пуста! Элемент добавлен как наименьший.");
                }
                Stack<MusicBand> bufCollection = collectionManager.getCollection();
                bufCollection.sort(Comparator.naturalOrder());
                if (band.compareTo(bufCollection.firstElement()) < 0) {
                    collectionManager.add(band);
                    return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
                } else {
                    return new ExecutionStatus(true, "Элемент не является наименьшим в коллекции!");
                }
            } else {
                return new ExecutionStatus(false, "Введены некорректные данные!");
            }
        } catch (Asker.Breaker e) {
            return new ExecutionStatus(false, "Ввод был прерван пользователем!");
        }
    }
}