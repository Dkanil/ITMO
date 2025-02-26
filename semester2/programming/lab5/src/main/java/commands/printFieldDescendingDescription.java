package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для вывода значений поля description всех элементов в порядке убывания.
 */
public class printFieldDescendingDescription extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды printFieldDescendingDescription.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public printFieldDescendingDescription(Console console, CollectionManager collectionManager) {
        super("print_field_descending_description", "вывести значения поля description всех элементов в порядке убывания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду вывода значений поля description всех элементов в порядке убывания.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        try {
            if (!argument.isEmpty()) {
                return new ExecutionStatus(false, "У команды print_field_descending_description не должно быть аргументов!\nПример корректного ввода: " + getName());
            }
            if (collectionManager.getBands().isEmpty()) {
                return new ExecutionStatus(false, "Коллекция пуста!");
            }
            collectionManager.sort();
            for (int i = collectionManager.getBands().size() - 1; i >= 0; i--) {
                console.println(collectionManager.getBands().get(i).getDescription());
            }
            return new ExecutionStatus(true, "Операция выполнена!");
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при выводе описаний!");
        }
    }
}