package commands.idArgumentCommands;

import commands.*;
import managers.*;
import utility.*;

/**
 * Класс команды для обновления значения элемента коллекции по его id.
 */
public class Update extends IdArgumentCommand implements Asking {

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
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            Long id = Long.parseLong(argument);
            console.println("Обновление элемента коллекции...");

            Pair validationStatusPair = validate(console, id);
            if (validationStatusPair.getExecutionStatus().isSuccess()) {
                collectionManager.removeById(id);
                collectionManager.add(validationStatusPair.getBand());
            }
            return validationStatusPair.getExecutionStatus();
        } else {
            return ArgumentStatus;
        }
    }
}