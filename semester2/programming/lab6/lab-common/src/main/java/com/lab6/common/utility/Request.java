package com.lab6.common.utility;

import com.lab6.common.models.MusicBand;
import com.lab6.common.validators.ArgumentValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Map<String, ArgumentValidator> commandsData;
    private String string;
    private Command<?> commandType;
    private MusicBand band = null;

    public Request(Map<String, ArgumentValidator> commandsData) {
        this.commandsData = commandsData;
    }

    public Request(String string) {
        this.string = string;
    }

    public Request(String string, MusicBand band) {
        this.string = string;
        this.band = band;
    }

    public Request(Command<?> commandType) {
        this.commandType = commandType;
    }

    public Command<?> getCommandType() {
        return commandType;
    }

    public String[] getCommand() {
        String[] inputCommand = (string.trim() + " ").split(" ", 2);
        inputCommand[1] = inputCommand[1].trim();
        return inputCommand;
    }

    public String getCommandString() {
        return string;
    }

    public MusicBand getBand() {
        return band;
    }

    public void setCommand(String command) {
        this.string = command;
    }

    public void setBand(MusicBand band) {
        this.band = band;
    }

    public Map<String, ArgumentValidator> getCommandsMap() {
        return commandsData;
    }

    @Override
    public String toString() {
        return "Request{" +
                "string='" + string + '\'' +
                ", commandType=" + commandType +
                ", band=" + band +
                '}';
    }
}
