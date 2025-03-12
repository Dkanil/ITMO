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

    /**
     * Выполняет команду добавления нового элемента в коллекцию.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus runInternal(Pair validationStatusPair) {
        collectionManager.add(validationStatusPair.getBand());
        return validationStatusPair.getExecutionStatus();
    }
}