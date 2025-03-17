package commands.askingCommands;

import commands.*;
import commands.validators.IdValidator;
import managers.CollectionManager;
import models.MusicBand;
import utility.*;

/**
 * Класс команды для обновления значения элемента коллекции по его id.
 */
public class Update extends AskingCommand<IdValidator> {
    /**
     * Конструктор команды update.
     *
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Update(Console console, CollectionManager collectionManager) {
        super(CommandNames.UPDATE.getName() + " id {element}", CommandNames.UPDATE.getDescription(), console, new IdValidator(collectionManager), collectionManager);
    }

    /**
     * Выполняет команду обновления элемента коллекции.
     *
     * @param band Элемент коллекции.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(MusicBand band) {
        collectionManager.removeById(band.getId());
        collectionManager.add(band);
        return new ExecutionStatus(true, "Элемент успешно обновлён!");
    }
}