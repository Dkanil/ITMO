package com.lab6.common.managers;

import com.lab6.common.utility.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, управляющий регистрацией и получением команд.
 */
public class CommandManager {
    private Map<String, Command<?>> commands = new HashMap<>();

    /**
     * Регистрирует команду.
     * @param commandName имя команды
     * @param command объект команды
     */
    public void register(String commandName, Command<?> command) {
        commands.put(commandName, command);
    }

    public void setCommandsMap(Map<String, Command<?>> commands) {
        this.commands = commands;
    }

    /**
     * Возвращает карту команд.
     * @return карта команд
     */
    public Map<String, Command<?>> getCommandsMap() {
        return commands;
    }

    /**
     * Возвращает команду по её имени.
     * @param commandName имя команды
     * @return объект команды
     */
    public Command<?> getCommand(String commandName) {
        return commands.get(commandName);
    }
}
