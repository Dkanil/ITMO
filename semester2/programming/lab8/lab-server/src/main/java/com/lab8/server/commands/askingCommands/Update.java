package com.lab8.server.commands.askingCommands;

import com.lab8.common.utility.Pair;
import com.lab8.server.utility.AskingCommand;
import com.lab8.server.utility.CommandNames;
import com.lab8.common.validators.IdValidator;
import com.lab8.common.models.MusicBand;
import com.lab8.common.utility.ExecutionStatus;

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
    protected ExecutionStatus runInternal(MusicBand band, Pair<String, String> user) {
        return collectionManager.update(band, user);
    }
}
