package com.lab6.common.utility;

import com.lab6.common.models.MusicBand;

import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String command;
    private MusicBand band;

    public Request(String command, MusicBand band) {
        this.command = command;
        this.band = band;
    }

    public String[] getCommand() {
        String[] inputCommand = (command.trim() + " ").split(" ", 2);
        inputCommand[1] = inputCommand[1].trim();
        return inputCommand;
    }

    public String getCommandString() {
        return command;
    }

    public MusicBand getBand() {
        return band;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setBand(MusicBand band) {
        this.band = band;
    }

    @Override
    public String toString() {
        return "Request{" +
                "command='" + command + '\'' +
                ", band=" + band +
                '}';
    }

}
