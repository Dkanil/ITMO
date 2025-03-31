package com.lab6.common.commands;

import com.lab6.common.commands.validators.IdValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.managers.CollectionManager;

/**
 * Класс команды для удаления элемента из коллекции по его id.
 */
public class RemoveById extends Command<IdValidator> {
    private final CollectionManager collectionManager;
    /**
     * Конструктор команды removeById.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveById(Console console, CollectionManager collectionManager) {
        super(CommandNames.REMOVE_BY_ID.getName() + " id", CommandNames.REMOVE_BY_ID.getDescription(), console, new IdValidator(collectionManager));
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления элемента коллекции по его id.
     * @param argument Аргумент команды, содержащий id элемента.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        Long id = Long.parseLong(argument);
        collectionManager.removeById(id);
        return new ExecutionStatus(true, "Элемент успешно удален!");
    }
}
