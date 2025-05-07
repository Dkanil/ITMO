package com.lab6.server.commands.askingCommands;

import com.lab6.server.utility.AskingCommand;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.EmptyValidator;
import com.lab6.common.models.MusicBand;
import com.lab6.common.utility.ExecutionStatus;

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
    protected ExecutionStatus runInternal(MusicBand band) {
        collectionManager.add(band);
        return new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!");
    }
}
