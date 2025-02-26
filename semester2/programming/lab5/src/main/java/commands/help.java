package commands;

import utility.*;
import managers.*;

/**
 * Класс команды для вывода справки по доступным командам.
 */
public class help extends Command {
    private final Console console;
    private final CommandManager commandManager;

    /**
     * Конструктор команды help.
     * @param console Консоль для ввода/вывода.
     * @param commandManager Менеджер команд.
     */
    public help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду вывода справки по доступным командам.
     * @param arg Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String arg) {
        if (!arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды help нет аргументов!\nПример корректного ввода: " + getName());
        }
        console.println("Список доступных команд:");
        for (var command : commandManager.getCommandsMap().entrySet()) {
            console.println(command.getValue().getName() + " - " + command.getValue().getDescription());
        }
        return new ExecutionStatus(true, "Справка по командам успешно выведена!");
    }
}