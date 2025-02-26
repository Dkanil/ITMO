package commands.emptyArgumentCommands;

import commands.CommandNames;
import managers.*;
import utility.*;

/**
 * Класс команды для вывода информации о коллекции.
 */
public class Info extends NoArgumentCommand {
    CollectionManager collectionManager;

    /**
     * Конструктор команды info.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public Info(Console console, CollectionManager collectionManager) {
        super(CommandNames.INFO.getName(), CommandNames.INFO.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода информации о коллекции.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            console.println("Тип коллекции: " + collectionManager.getBands().getClass().getName());
            console.println("Дата инициализации: " + collectionManager.getInitializationDate());
            console.println("Дата последнего сохранения: " + collectionManager.getLastSaveDate());
            console.println("Количество элементов: " + collectionManager.getBands().size());
            return new ExecutionStatus(true, "Информация о коллекции успешно выведена!");
        } else {
            return ArgumentStatus;
        }
    }
}