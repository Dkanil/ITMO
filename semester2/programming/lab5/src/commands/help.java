package commands;

import utility.*;
import managers.*;


public class help extends Command {
    private final Console console;
    private final CommandManager commandManager;

    public help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    @Override
    public ExecutionStatus run(String arg) {
        if (!arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды help нет аргументов!\nПример корректного ввода: " + getName());
        }
        console.println("Список доступных команд:");
        for (var command : commandManager.getCommandsMap().entrySet()) {
            console.println(command.getValue().getName() + " - " + command.getValue().getDescription());
        }
        return new ExecutionStatus(true, "");
    }
}
