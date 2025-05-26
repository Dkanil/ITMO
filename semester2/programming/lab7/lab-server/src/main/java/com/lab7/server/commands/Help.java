package com.lab7.server.commands;

import com.lab7.common.utility.Pair;
import com.lab7.server.managers.CommandManager;
import com.lab7.server.managers.DBManager;
import com.lab7.server.utility.Command;
import com.lab7.server.utility.CommandNames;
import com.lab7.common.validators.EmptyValidator;
import com.lab7.common.utility.ExecutionStatus;

import java.util.stream.Collectors;
import java.util.LinkedHashMap;

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
        commandManager.getCommandsMap().entrySet().stream()
                .collect(Collectors.groupingBy(entry -> CommandNames.valueOf(entry.getKey().toUpperCase()).getRequiredPermission(),
                        LinkedHashMap::new, Collectors.toList())) // Группировка по типу прав доступа
                .forEach((permission, commands) -> {
                    helpMessage.append("Необходимые права для выполнения: ").append(permission).append("\n");
                    commands.forEach(entry -> helpMessage.append("  ")
                            .append(entry.getValue().getName())
                            .append(" - ")
                            .append(entry.getValue().getDescription())
                            .append("\n"));
                });
        helpMessage.append("Справка по командам успешно выведена!\n")
                .append("Вы вошли, как ")
                .append(user.getFirst())
                .append(", ваш уровень прав: ");

        String permissionLevel = DBManager.getInstance().checkUserPermission(user).getMessage();
        helpMessage.append(permissionLevel);

        if (permissionLevel.equals("ABOBA")) {
            helpMessage.append("\nАХАХАХАХАХАХАХААХ, У ТЕБЯ НЕТ ПРАВ!!!!!");
        }
        return new ExecutionStatus(true, helpMessage.toString());
    }
}
