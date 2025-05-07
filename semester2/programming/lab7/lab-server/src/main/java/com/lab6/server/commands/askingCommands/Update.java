package com.lab6.server.commands.askingCommands;

import com.lab6.server.utility.AskingCommand;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.IdValidator;
import com.lab6.common.models.MusicBand;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Класс команды для обновления значения элемента коллекции по его id.
 */
public class Update extends AskingCommand<IdValidator> {
    /**
     * Конструктор команды update.
     */
    public Update() {
        super(CommandNames.UPDATE.getName() + " id {element}", CommandNames.UPDATE.getDescription(), new IdValidator());
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
