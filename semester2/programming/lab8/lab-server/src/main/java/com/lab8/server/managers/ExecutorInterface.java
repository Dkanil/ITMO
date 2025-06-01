package com.lab8.server.managers;

import com.lab8.common.models.MusicBand;
import com.lab8.common.utility.ExecutionStatus;
import com.lab8.common.utility.Pair;

public interface ExecutorInterface {
    ExecutionStatus runCommand(String[] userCommand, MusicBand musicBand, Pair<String, String> user);
}
