package com.lab8.server.commands;

import com.lab8.common.utility.Pair;
import com.lab8.server.utility.Command;
import com.lab8.server.utility.CommandNames;
import com.lab8.common.validators.IdValidator;
import com.lab8.common.utility.ExecutionStatus;

/**
 * Класс команды для удаления элемента из коллекции по его id.
 */
public class RemoveById extends Command<IdValidator> {
    /**
     * Конструктор команды removeById.
     */
    public RemoveById() {
        super(CommandNames.REMOVE_BY_ID.getName() + " id", CommandNames.REMOVE_BY_ID.getDescription(), new IdValidator());
    }

    /**
     * Выполняет команду удаления элемента коллекции по его id.
     * @param argument Аргумент команды, содержащий id элемента.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        Long id = Long.parseLong(argument);
        if (collectionManager.getById(id) == null) {
            return new ExecutionStatus(false, "Элемент с указанным id не найден!");
        }
        return collectionManager.removeById(id, user);
    }
}
