package managers;

import commands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static Map<String, Command> commands = new HashMap<>();

    public static void register(String commandName, Command command) {
        commands.put(commandName, command);
    }
    public static Map<String, Command> getCommandsMap() {
        return commands;
    }

    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
