package commands.emptyArgumentCommands;

import commands.CommandNames;
import utility.*;
import managers.*;

/**
 * Класс команды для сохранения коллекции в файл.
 */
public class Save extends NoArgumentCommand {
    CollectionManager collectionManager;

    /**
     * Конструктор команды save.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Save(utility.Console console, managers.CollectionManager collectionManager) {
        super(CommandNames.SAVE.getName(), CommandNames.SAVE.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сохранения коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            collectionManager.saveCollection();
            return new ExecutionStatus(true, "Коллекция успешно сохранена!");
        } else {
            return ArgumentStatus;
        }
    }
}