package com.lab6.server.commands.askingCommands;

import com.lab6.server.commands.Command;
import com.lab6.server.commands.validators.ArgumentValidator;
import com.lab6.server.commands.validators.ElementValidator;
import com.lab6.server.commands.validators.IdValidator;
import com.lab6.server.managers.CollectionManager;
import com.lab6.common.models.MusicBand;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Pair;

/**
 * Абстрактный класс для команд, требующих ввода данных.
 *
 * @param <T> Тип валидатора аргументов.
 */
public abstract class AskingCommand<T extends ArgumentValidator> extends Command<T> {
    protected final CollectionManager collectionManager;

    /**
     * Конструктор команды AskingCommand.
     *
     * @param name Имя команды.
     * @param description Описание команды.
     * @param console Консоль для ввода/вывода.
     * @param argumentValidator Валидатор аргументов команды.
     * @param collectionManager Менеджер коллекции.
     */
    public AskingCommand(String name, String description, Console console, T argumentValidator, CollectionManager collectionManager) {
        super(name, description, console, argumentValidator);
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
    @Override
    public ExecutionStatus run(String arg) {
        ExecutionStatus argumentStatus = argumentValidator.validate(arg, getName());
        if (argumentStatus.isSuccess()) {
            Long id;
            if (argumentValidator instanceof IdValidator) {
                id = Long.parseLong(arg);
            } else {
                id = collectionManager.getFreeId();
            }
            ElementValidator elementValidator = new ElementValidator();
            Pair<ExecutionStatus, MusicBand> validationStatusPair = elementValidator.validateAsking(console, id);
            if (!validationStatusPair.getFirst().isSuccess()) {
                return validationStatusPair.getFirst();
            } else {
                return runInternal(validationStatusPair.getSecond());
            }
        } else {
            return argumentStatus;
        }
    }
}
