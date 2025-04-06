package com.lab6.server.commands.askingCommands;

import com.lab6.server.commands.CommandNames;
import com.lab6.server.commands.validators.IdValidator;
import com.lab6.server.managers.CollectionManager;
import com.lab6.common.models.MusicBand;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;

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
