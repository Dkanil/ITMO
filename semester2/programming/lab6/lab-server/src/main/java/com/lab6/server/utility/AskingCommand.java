package com.lab6.server.utility;

import com.lab6.server.managers.CollectionManager;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.validators.ArgumentValidator;
import com.lab6.common.validators.IdValidator;
import com.lab6.common.models.MusicBand;

/**
 * Абстрактный класс для команд, требующих ввода данных.
 * @param <T> Тип валидатора аргументов.
 */
public abstract class AskingCommand<T extends ArgumentValidator> extends Command<T> {
    protected final CollectionManager collectionManager;

    /**
     * Конструктор команды AskingCommand.
     *
     * @param name Имя команды.
     * @param description Описание команды.
     * @param argumentValidator Валидатор аргументов команды.
     * @param collectionManager Менеджер коллекции.
     */
    public AskingCommand(String name, String description, T argumentValidator, CollectionManager collectionManager) {
        super(name, description, argumentValidator);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду с аргументом.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String arg) {
        return null;
    }

    /**
     * Выполняет команду с элементом коллекции.
     *
     * @param band Элемент коллекции.
     * @return Статус выполнения команды.
     */
    protected abstract ExecutionStatus runInternal(MusicBand band);

    /**
     * Запускает выполнение команды.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    public ExecutionStatus run(String arg, MusicBand band) {
        ExecutionStatus argumentStatus = argumentValidator.validate(arg, getName());
        if (argumentStatus.isSuccess()) {
            Long id;
            if (argumentValidator instanceof IdValidator) {
                id = Long.parseLong(arg);
                if (collectionManager.getById(id) == null) {
                    return new ExecutionStatus(false, "Элемент с указанным id не найден!");
                }
            } else {
                id = collectionManager.getFreeId();
            }
            band.updateId(id);
            return runInternal(band);
        } else {
            return argumentStatus;
        }
    }

    @Override
    public ExecutionStatus run(String arg) {
        return new ExecutionStatus(false, "Метод должен вызываться с аргументом MusicBand!");
    }
}
