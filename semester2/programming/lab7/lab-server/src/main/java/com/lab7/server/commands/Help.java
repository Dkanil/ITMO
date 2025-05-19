package com.lab7.server.commands;

import com.lab7.common.utility.Pair;
import com.lab7.server.managers.CommandManager;
import com.lab7.server.utility.Command;
import com.lab7.server.utility.CommandNames;
import com.lab7.common.validators.EmptyValidator;
import com.lab7.common.utility.ExecutionStatus;

/**
 * Класс команды для вывода справки по доступным командам.
 */
public class Help extends Command<EmptyValidator> {
    private final CommandManager commandManager;

    /**
     * Конструктор команды help.
     * @param commandManager Менеджер команд.
     */
    public Help(CommandManager commandManager) {
        super(CommandNames.HELP.getName(), CommandNames.HELP.getDescription(), new EmptyValidator());
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду вывода справки по доступным командам.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    protected ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        StringBuilder helpMessage = new StringBuilder("Список доступных команд:\n");
        for (var command : commandManager.getCommandsMap().entrySet()) {
            helpMessage.append(command.getValue().getName())
                    .append(" - ")
                    .append(command.getValue().getDescription())
                    .append("\n");
        }
        helpMessage.append("Справка по командам успешно выведена!");
        return new ExecutionStatus(true, helpMessage.toString());
    }
}
