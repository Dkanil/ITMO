package com.lab8.server.commands.askingCommands;

import com.lab8.common.utility.Pair;
import com.lab8.server.utility.AskingCommand;
import com.lab8.server.utility.CommandNames;
import com.lab8.common.validators.EmptyValidator;
import com.lab8.common.models.MusicBand;
import com.lab8.common.utility.ExecutionStatus;

/**
 * Класс команды для добавления нового элемента в коллекцию.
 */
public class Add extends AskingCommand<EmptyValidator> {
    /**
     * Конструктор команды add.
     */
    public Add() {
        super(CommandNames.ADD.getName() + " {element}", CommandNames.ADD.getDescription(), new EmptyValidator());
    }

    /**
     * Выполняет внутреннюю часть команды добавления.
     * @param band Музыкальная группа, которую нужно добавить.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(MusicBand band, Pair<String, String> user) {
        return collectionManager.add(band, user);
    }
}
