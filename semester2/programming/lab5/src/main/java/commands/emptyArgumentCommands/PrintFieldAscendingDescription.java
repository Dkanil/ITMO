package commands.emptyArgumentCommands;

import commands.CommandNames;
import utility.*;
import managers.*;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке возрастания.
 */
public class PrintFieldAscendingDescription extends NoArgumentCommand {
    CollectionManager collectionManager;

    /**
     * Конструктор команды printFieldAscendingDescription.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public PrintFieldAscendingDescription(Console console, CollectionManager collectionManager) {
        super(CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getName(), CommandNames.PRINT_FIELD_ASCENDING_DESCRIPTION.getDescription(), console);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значений поля description всех элементов в порядке возрастания.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus runInternal(String argument) {
        if (collectionManager.getBands().isEmpty()) {
            return new ExecutionStatus(false, "Коллекция пуста!");
        }
        collectionManager.sort();
        for (var band : collectionManager.getBands()) {
            console.println(band.getDescription());
        }
        return new ExecutionStatus(true, "Операция выполнена!");
    }
}