package commands.emptyArgumentCommands;

import commands.CommandNames;
import utility.*;
import managers.*;

/**
 * Класс команды для очистки коллекции.
 */
public class Clear extends NoArgumentCommand {
    CollectionManager collectionManager;

    /**
     * Конструктор команды clear.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Clear(Console console, CollectionManager collectionManager) {
        super(CommandNames.CLEAR.getName(), CommandNames.CLEAR.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду очистки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            collectionManager.clear();
            return new ExecutionStatus(true, "Коллекция успешно очищена!");
        } else {
            return ArgumentStatus;
        }
    }
}