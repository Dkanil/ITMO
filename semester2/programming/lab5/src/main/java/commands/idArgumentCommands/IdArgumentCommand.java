package commands.idArgumentCommands;

import commands.*;
import managers.CollectionManager;
import utility.*;

public abstract class IdArgumentCommand extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды с аргументом id.
     * @param name Имя команды.
     * @param description Описание команды.
     * @param console Консоль для ввода/вывода.
     */
    public IdArgumentCommand(String name, String description, Console console, CollectionManager collectionManager) {
        super(name, description);
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Проверяет корректность аргумента команды.
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        if (arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды должен быть аргумент (id элемента коллекции)!\nПример корректного ввода: " + getName());
        }
        try {
            Long id = Long.parseLong(arg);
            if (collectionManager.getById(id) == null) {
                return new ExecutionStatus(false, "Элемент с указанным id не найден!");
            }
        } catch (NumberFormatException e) {
            return new ExecutionStatus(false, "Формат аргумента неверен! Он должен быть целым числом.");
        }
        return new ExecutionStatus(true, "Аргумент команды введен корректно.");
    }
}
