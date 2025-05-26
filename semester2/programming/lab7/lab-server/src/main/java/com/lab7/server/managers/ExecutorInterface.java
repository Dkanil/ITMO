package com.lab7.server.managers;

import com.lab7.common.models.MusicBand;
import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;

public interface ExecutorInterface {
    ExecutionStatus runCommand(String[] userCommand, MusicBand musicBand, Pair<String, String> user);
}
