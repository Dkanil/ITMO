package commands.emptyArgumentCommands;

import commands.*;
import models.MusicBand;
import utility.*;
import managers.*;

import java.util.Comparator;
import java.util.Stack;

/**
 * Класс команды для добавления нового элемента в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
 */
public class AddIfMin extends NoArgumentCommand implements Asking {
    CollectionManager collectionManager;

    /**
     * Конструктор команды addIfMin.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public AddIfMin(Console console, CollectionManager collectionManager) {
        super(CommandNames.ADD_IF_MIN.getName() + " {element}", CommandNames.ADD_IF_MIN.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду добавления нового элемента в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            console.println("Добавление элемента в коллекцию...");
            Asking.Pair validationStatusPair = validate(console, collectionManager.getFreeId());
            ExecutionStatus executionStatus = validationStatusPair.getExecutionStatus();
            MusicBand band = validationStatusPair.getBand();
            if (executionStatus.isSuccess()) {
                if (collectionManager.getCollection().isEmpty()) {
                    collectionManager.add(band);
                    return new ExecutionStatus(true, "Коллекция пуста! Элемент добавлен как наименьший.");
                }
                Stack<MusicBand> bufCollection = collectionManager.getCollection();
                bufCollection.sort(Comparator.naturalOrder());
                if (band.compareTo(bufCollection.firstElement()) < 0) {
                    collectionManager.add(band);
                    return executionStatus;
                } else {
                    return new ExecutionStatus(true, "Элемент не является наименьшим в коллекции!");
                }
            } else {
                return executionStatus;
            }
        } else {
            return ArgumentStatus;
        }
    }
}