package commands;

import commands.validators.EmptyValidator;
import utility.*;
import managers.*;

/**
 * Класс команды для сохранения коллекции в файл.
 */
public class Save extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды save.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Save(utility.Console console, managers.CollectionManager collectionManager) {
        super(CommandNames.SAVE.getName(), CommandNames.SAVE.getDescription(), console, new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду сохранения коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        collectionManager.saveCollection();
        return new ExecutionStatus(true, "Коллекция успешно сохранена!");
    }
}