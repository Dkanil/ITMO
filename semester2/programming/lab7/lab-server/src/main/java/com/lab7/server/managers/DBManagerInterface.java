package com.lab7.server.managers;

import com.lab7.common.models.MusicBand;
import com.lab7.common.models.MusicGenre;
import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.server.utility.Transactional;

import java.sql.SQLException;
import java.util.Stack;

public interface DBManagerInterface {
    ExecutionStatus addUser(Pair<String, String> user);
    ExecutionStatus checkPassword(Pair<String, String> user);
    ExecutionStatus clear(Pair<String, String> user);
    ExecutionStatus removeById(Long id, Pair<String, String> user);
    ExecutionStatus removeAllByGenre(MusicGenre genre, Pair<String, String> user);

    @Transactional
    ExecutionStatus addMusicBand(MusicBand band, Pair<String, String> user) throws SQLException;

    @Transactional
    ExecutionStatus updateMusicBand(MusicBand band, Pair<String, String> user) throws SQLException;

    ExecutionStatus loadCollection(Stack<MusicBand> collection);

}
