package com.lab6.common.utility;

import com.lab6.common.models.MusicBand;
import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 11L;
    private String string;
    private MusicBand band = null;

    public Request(String string) {
        this.string = string;
    }

    public Request(String string, MusicBand band) {
        this.string = string;
        this.band = band;
    }

    public String[] getCommand() {
        String[] inputCommand = (string.trim() + " ").split(" ", 2);
        inputCommand[1] = inputCommand[1].trim();
        return inputCommand;
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

    @Override
    public String toString() {
        return "Request{" +
                "string='" + string + '\'' +
                ", band=" + band +
                '}';
    }
}
