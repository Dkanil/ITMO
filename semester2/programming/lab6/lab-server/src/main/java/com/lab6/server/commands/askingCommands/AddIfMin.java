package com.lab6.server.commands.askingCommands;

import com.lab6.server.commands.CommandNames;
import com.lab6.server.commands.validators.EmptyValidator;
import com.lab6.common.models.MusicBand;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Console;
import com.lab6.server.managers.CollectionManager;

import java.util.Comparator;
import java.util.Stack;

/**
 * Класс команды для добавления нового элемента в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
 */
public class AddIfMin extends AskingCommand<EmptyValidator> {
    /**
     * Конструктор команды addIfMin.
     *
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public AddIfMin(Console console, CollectionManager collectionManager) {
        super(CommandNames.ADD_IF_MIN.getName() + " {element}", CommandNames.ADD_IF_MIN.getDescription(), console, new EmptyValidator(), collectionManager);
    }

    /**
     * Выполняет команду добавления нового элемента в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции.
     *
     * @param band Элемент коллекции.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(MusicBand band) {
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
    }
}
