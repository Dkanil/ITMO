package commands;

import commands.validators.EmptyValidator;
import utility.*;
import managers.*;

/**
 * Класс команды для очистки коллекции.
 */
public class Clear extends Command<EmptyValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды clear.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Clear(Console console, CollectionManager collectionManager) {
        super(CommandNames.CLEAR.getName(), CommandNames.CLEAR.getDescription(), console, new EmptyValidator());
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду очистки коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        collectionManager.clear();
        return new ExecutionStatus(true, "Коллекция успешно очищена!");
    }
}