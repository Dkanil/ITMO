package com.lab8.common.utility;

import com.lab8.common.models.MusicBand;
import java.io.Serial;
import java.io.Serializable;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 11L;
    private final String string;
    private MusicBand band = null;
    private final Pair<String, String> user;

    public Request(String string, Pair<String, String> user) {
        this.string = string;
        this.user = user;
    }

    public Request(String string, MusicBand band, Pair<String, String> user) {
        this.string = string;
        this.band = band;
        this.user = user;
    }

    public Pair<String, String> getUser() { return user; }

    public String[] getCommand() {
        String[] inputCommand = (string.trim() + " ").split(" ", 2);
        inputCommand[1] = inputCommand[1].trim();
        return inputCommand;
    }

    public MusicBand getBand() {
        return band;
    }

    @Override
    public String toString() {
        return "Request{" +
                "string='" + string + '\'' +
                ", band=" + band +
                ", user=" + user +
                '}';
    }
}
