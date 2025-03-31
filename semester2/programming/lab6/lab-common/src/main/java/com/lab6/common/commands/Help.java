package com.lab6.common.commands;

import com.lab6.common.commands.validators.EmptyValidator;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.managers.CommandManager;

/**
 * Класс команды для вывода справки по доступным командам.
 */
public class Help extends Command<EmptyValidator> {
    private final CommandManager commandManager;

    /**
     * Конструктор команды help.
     * @param console Консоль для ввода/вывода.
     * @param commandManager Менеджер команд.
     */
    public Help(Console console, CommandManager commandManager) {
        super(CommandNames.HELP.getName(), CommandNames.HELP.getDescription(), console, new EmptyValidator());
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду вывода справки по доступным командам.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument) {
        console.println("Список доступных команд:");
        for (var command : commandManager.getCommandsMap().entrySet()) {
            console.println(command.getValue().getName() + " - " + command.getValue().getDescription());
        }
        return new ExecutionStatus(true, "Справка по командам успешно выведена!");
    }
}
