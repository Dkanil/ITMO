package commands.idArgumentCommands;

import commands.*;
import managers.*;
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

    /**
     * Выполняет команду обновления элемента коллекции.
     * @param argument Аргумент команды, содержащий id элемента.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus runInternal(Pair validationStatusPair) {
        collectionManager.removeById(validationStatusPair.getBand().getId());
        collectionManager.add(validationStatusPair.getBand());
        return new ExecutionStatus(true, "Элемент успешно обновлён!");
    }
}