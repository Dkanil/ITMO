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
public class AddIfMin extends NoArgumentAskingCommand{
    /**
     * Конструктор команды addIfMin.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public AddIfMin(Console console, CollectionManager collectionManager) {
        super(CommandNames.ADD_IF_MIN.getName() + " {element}", CommandNames.ADD_IF_MIN.getDescription(), console, collectionManager);
    }

    @Override
    public ExecutionStatus runInternal(Pair<ExecutionStatus, MusicBand> validationStatusPair) {
        MusicBand band = validationStatusPair.getSecond();
        if (collectionManager.getCollection().isEmpty()) {
            collectionManager.add(band);
            return new ExecutionStatus(true, "Коллекция пуста! Элемент добавлен как наименьший.");
        }
        Stack<MusicBand> bufCollection = collectionManager.getCollection();
        bufCollection.sort(Comparator.naturalOrder());
        if (band.compareTo(bufCollection.firstElement()) < 0) {
            collectionManager.add(band);
            return validationStatusPair.getFirst();
        } else {
            return new ExecutionStatus(true, "Элемент не является наименьшим в коллекции!");
        }
    }
}