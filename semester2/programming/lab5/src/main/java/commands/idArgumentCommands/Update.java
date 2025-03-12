package commands.idArgumentCommands;

import commands.*;
import managers.*;
import models.MusicBand;
import utility.*;

/**
 * Класс команды для обновления значения элемента коллекции по его id.
 */
public class Update extends IdArgumentAskingCommand {

    /**
     * Конструктор команды update.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Update(Console console, CollectionManager collectionManager) {
        super(CommandNames.UPDATE.getName() + " id {element}", CommandNames.UPDATE.getDescription(), console, collectionManager);
    }

    @Override
    public ExecutionStatus runInternal(Pair<ExecutionStatus, MusicBand> validationStatusPair) {
        collectionManager.removeById(validationStatusPair.getSecond().getId());
        collectionManager.add(validationStatusPair.getSecond());
        return new ExecutionStatus(true, "Элемент успешно обновлён!");
    }
}