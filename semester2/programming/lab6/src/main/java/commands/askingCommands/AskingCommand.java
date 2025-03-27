package commands.askingCommands;

import commands.*;
import commands.validators.*;
import managers.CollectionManager;
import models.MusicBand;
import utility.*;

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
        ExecutionStatus ArgumentStatus = argumentValidator.validate(arg, getName());
        if (ArgumentStatus.isSuccess()) {
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
            return ArgumentStatus;
        }
    }
}