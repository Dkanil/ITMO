package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке возрастания.
 */
public class printFieldAscendingDescription extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды printFieldAscendingDescription.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public printFieldAscendingDescription(Console console, CollectionManager collectionManager) {
        super("print_field_ascending_description", "вывести значения поля description всех элементов в порядке возрастания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значений поля description всех элементов в порядке возрастания.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionStatus(false, "У команды print_field_ascending_description не должно быть аргументов!\nПример корректного ввода: " + getName());
            }
            if (collectionManager.getBands().isEmpty()) {
                return new ExecutionStatus(false, "Коллекция пуста!");
            }
            collectionManager.sort();
            for (var band : collectionManager.getBands()) {
                console.println(band.getDescription());
            }
            return new ExecutionStatus(true, "Операция выполнена!");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выводе описаний!");
        }
    }
}