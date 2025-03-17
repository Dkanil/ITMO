package commands.askingCommands;

import commands.*;
import commands.validators.EmptyValidator;
import models.MusicBand;
import utility.*;
import managers.CollectionManager;

/**
 * Класс команды для добавления нового элемента в коллекцию.
 */
public class Add extends AskingCommand<EmptyValidator> {
    /**
     * Конструктор команды add.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Add(Console console, CollectionManager collectionManager) {
        super(CommandNames.ADD.getName() + " {element}", CommandNames.ADD.getDescription(), console, new EmptyValidator(), collectionManager);
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