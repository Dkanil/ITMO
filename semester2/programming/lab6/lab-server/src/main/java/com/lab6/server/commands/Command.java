package com.lab6.server.commands;

import com.lab6.server.commands.validators.ArgumentValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Pair;

/**
 * Абстрактный класс для всех команд.
 *
 * @param <T> Тип валидатора аргументов, который должен расширять {@link ArgumentValidator}.
 */
public abstract class Command<T extends ArgumentValidator> {
    private final Pair<String, String> nameAndDescription;
    protected Console console;
    protected final T argumentValidator;

    /**
     * Конструктор команды.
     *
     * @param name Имя команды.
     * @param description Описание команды.
     * @param console Консоль ввода-вывода.
     * @param argumentValidator Валидатор аргументов.
     */
    public Command(String name, String description, Console console, T argumentValidator) {
        this.nameAndDescription = new Pair<>(name, description);
        this.console = console;
        this.argumentValidator = argumentValidator;
    }

    /**
     * Возвращает имя команды.
     *
     * @return Имя команды.
     */
    public String getName() {
        return nameAndDescription.getFirst();
    }

    /**
     * Возвращает описание команды.
     *
     * @return Описание команды.
     */
    public String getDescription() {
        return nameAndDescription.getSecond();
    }

    /**
     * Обновляет консоль ввода-вывода.
     *
     * @param console Консоль ввода-вывода.
     */
    public void updateConsole(Console console) {
        this.console = console;
    }

    /**
     * Выполняет команду с проверкой аргумента. В случае успешной проверки вызывает метод {@link #runInternal(String)}.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    public ExecutionStatus run(String arg) {
        ExecutionStatus argumentStatus = argumentValidator.validate(arg, getName());
        if (argumentStatus.isSuccess()) {
            return runInternal(arg);
        } else {
            return argumentStatus;
        }
    }

    /**
     * Абстрактный метод для выполнения внутренней части команды.
     *
     * @param arg Аргумент команды.
     * @return Статус выполнения команды.
     */
    protected abstract ExecutionStatus runInternal(String arg);

    /**
     * Возвращает хэш-код команды.
     *
     * @return Хэш-код команды.
     */
    @Override
    public int hashCode() {
        return nameAndDescription.getFirst().hashCode() + nameAndDescription.getSecond().hashCode();
    }

    /**
     * Сравнивает текущую команду с другим объектом.
     *
     * @param object Объект для сравнения.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Command<?> command = (Command<?>) object;
        return nameAndDescription.getFirst().equals(command.nameAndDescription.getFirst()) &&
               nameAndDescription.getSecond().equals(command.nameAndDescription.getSecond());
    }

    /**
     * Возвращает строковое представление команды.
     *
     * @return Строковое представление команды.
     */
    @Override
    public String toString() {
        return "Command{" +
                "name='" + nameAndDescription.getFirst() + '\'' +
                ", description='" + nameAndDescription.getSecond() + '\'' +
                '}';
    }
}
