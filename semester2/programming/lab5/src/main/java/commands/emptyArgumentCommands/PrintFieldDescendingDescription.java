package commands.emptyArgumentCommands;

import commands.CommandNames;
import utility.*;
import managers.*;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке убывания.
 */
public class PrintFieldDescendingDescription extends NoArgumentCommand {
    CollectionManager collectionManager;

    /**
     * Конструктор команды printFieldDescendingDescription.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public PrintFieldDescendingDescription(Console console, CollectionManager collectionManager) {
        super(CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getName(), CommandNames.PRINT_FIELD_DESCENDING_DESCRIPTION.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значений поля description всех элементов в порядке убывания.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            if (collectionManager.getBands().isEmpty()) {
                return new ExecutionStatus(false, "Коллекция пуста!");
            }
            collectionManager.sort();
            for (int i = collectionManager.getBands().size() - 1; i >= 0; i--) {
                console.println(collectionManager.getBands().get(i).getDescription());
            }
            return new ExecutionStatus(true, "Операция выполнена!");
        } else {
            return ArgumentStatus;
        }
    }
}