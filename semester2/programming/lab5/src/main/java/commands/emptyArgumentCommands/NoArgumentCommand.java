package commands.emptyArgumentCommands;

import commands.*;
import utility.*;

/**
 * Абстрактный класс для команд, не принимающих аргументы.
 */
public abstract class NoArgumentCommand extends Command {
    Console console;

    /**
     * Конструктор команды без аргументов.
     * @param name Имя команды.
     * @param description Описание команды.
     * @param console Консоль для ввода/вывода.
     */
    public NoArgumentCommand(String name, String description, Console console) {
        super(name, description);
        this.console = console;
    }

    /**
     * Проверяет корректность аргумента команды.
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        if (!arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды нет аргументов!\nПример корректного ввода: " + getName());
        }
        return new ExecutionStatus(true, "Аргумент команды введен корректно.");
    }
}