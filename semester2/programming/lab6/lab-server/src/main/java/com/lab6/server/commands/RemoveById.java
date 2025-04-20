package com.lab6.server.commands;

import com.lab6.common.utility.Command;
import com.lab6.common.utility.CommandNames;
import com.lab6.common.validators.IdValidator;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для удаления элемента из коллекции по его id.
 */
public class RemoveById extends Command<IdValidator> {
    private final CollectionManager collectionManager;
    /**
     * Конструктор команды removeById.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveById(CollectionManager collectionManager) {
        super(CommandNames.REMOVE_BY_ID.getName() + " id", CommandNames.REMOVE_BY_ID.getDescription(), new IdValidator());
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
        if (collectionManager.getById(id) == null) {
            return new ExecutionStatus(false, "Элемент с указанным id не найден!");
        }
        collectionManager.removeById(id);
        return new ExecutionStatus(true, "Элемент успешно удален!");
    }
}
