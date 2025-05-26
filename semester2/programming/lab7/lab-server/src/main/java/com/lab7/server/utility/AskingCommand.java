package com.lab7.server.utility;

import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.common.utility.PermissionType;
import com.lab7.common.validators.ArgumentValidator;
import com.lab7.common.validators.IdValidator;
import com.lab7.common.models.MusicBand;
import com.lab7.server.managers.DBManager;

/**
 * Абстрактный класс для команд, требующих ввода данных.
 * @param <T> Тип валидатора аргументов.
 */
public abstract class AskingCommand<T extends ArgumentValidator> extends Command<T> {

    /**
     * Конструктор команды AskingCommand.
     *
     * @param name Имя команды.
     * @param description Описание команды.
     * @param argumentValidator Валидатор аргументов команды.
     */
    public AskingCommand(String name, String description, T argumentValidator) {
        super(name, description, argumentValidator);
    }

    /**
     * Выполняет команду с аргументом.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String arg, Pair<String, String> user) {
        return null;
    }

    /**
     * Выполняет команду с элементом коллекции.
     *
     * @param band Элемент коллекции.
     * @return Статус выполнения команды.
     */
    protected abstract ExecutionStatus runInternal(MusicBand band, Pair<String, String> user);

    /**
     * Запускает выполнение команды.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    public ExecutionStatus run(String arg, MusicBand band, Pair<String, String> user) {
        ExecutionStatus argumentStatus = argumentValidator.validate(arg, getName());
        if (argumentStatus.isSuccess()) {
            ExecutionStatus permissionStatus = checkPermission(user);
            if (!permissionStatus.isSuccess()) {
                return permissionStatus;
            }
            if (argumentValidator instanceof IdValidator) {
                Long id = Long.parseLong(arg);
                if (collectionManager.getById(id) == null) {
                    return new ExecutionStatus(false, "Элемент с указанным id не найден!");
                }
                band.updateId(id);
            }
            return runInternal(band, user);
        } else {
            return argumentStatus;
        }
    }

    @Override
    public ExecutionStatus run(String arg, Pair<String, String> user) {
        return new ExecutionStatus(false, "Метод должен вызываться с аргументом MusicBand!");
    }
}
