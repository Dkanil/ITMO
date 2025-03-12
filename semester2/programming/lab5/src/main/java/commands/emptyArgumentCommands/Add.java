package commands.emptyArgumentCommands;

import commands.*;
import models.MusicBand;
import utility.*;
import managers.*;

/**
 * Класс команды для добавления нового элемента в коллекцию.
 */
public class Add extends NoArgumentAskingCommand {
    /**
     * Конструктор команды add.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Add(Console console, CollectionManager collectionManager) {
        super(CommandNames.ADD.getName() + " {element}", CommandNames.ADD.getDescription(), console, collectionManager);
    }


    @Override
    public ExecutionStatus runInternal(Pair<ExecutionStatus, MusicBand> validationStatusPair) {
        collectionManager.add(validationStatusPair.getSecond());
        return validationStatusPair.getFirst();
    }
}