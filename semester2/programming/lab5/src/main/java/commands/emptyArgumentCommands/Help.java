package commands.emptyArgumentCommands;

import commands.CommandNames;
import utility.*;
import managers.*;

/**
 * Класс команды для вывода справки по доступным командам.
 */
public class Help extends NoArgumentCommand {
    private final CommandManager commandManager;

    /**
     * Конструктор команды help.
     * @param console Консоль для ввода/вывода.
     * @param commandManager Менеджер команд.
     */
    public Help(Console console, CommandManager commandManager) {
        super(CommandNames.HELP.getName(), CommandNames.HELP.getDescription(), console);
        this.commandManager = commandManager;
    }

    /**
     * Выполняет команду вывода справки по доступным командам.
     * @param argument Аргумент команды (не используется).
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus ArgumentStatus = validate(argument, getName());
        if (ArgumentStatus.isSuccess()) {
            console.println("Список доступных команд:");
            for (var command : commandManager.getCommandsMap().entrySet()) {
                console.println(command.getValue().getName() + " - " + command.getValue().getDescription());
            }
            return new ExecutionStatus(true, "Справка по командам успешно выведена!");
        } else {
            return ArgumentStatus;
        }
    }
}