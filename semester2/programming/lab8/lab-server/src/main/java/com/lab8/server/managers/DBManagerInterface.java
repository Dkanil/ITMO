package com.lab8.server.managers;

import com.lab8.common.models.MusicBand;
import com.lab8.common.models.MusicGenre;
import com.lab8.common.utility.ExecutionStatus;
import com.lab8.common.utility.Pair;
import com.lab8.common.utility.PermissionType;
import com.lab8.server.utility.Transactional;

import java.sql.SQLException;
import java.util.Stack;

public interface DBManagerInterface {
    ExecutionStatus addUser(Pair<String, String> user);
    ExecutionStatus checkPassword(Pair<String, String> user);
    ExecutionStatus showUserList(Pair<String, String> user);
    ExecutionStatus updateUserPermissions(String user, PermissionType permission);
    ExecutionStatus checkUserPermission(Pair<String, String> user);
    ExecutionStatus clear(Pair<String, String> user);
    ExecutionStatus clearAll();
    ExecutionStatus removeById(Long id, Pair<String, String> user);
    ExecutionStatus removeAllByGenre(MusicGenre genre, Pair<String, String> user);
    ExecutionStatus removeAllByGenre(MusicGenre genre);

    @Transactional
    ExecutionStatus addMusicBand(MusicBand band, Pair<String, String> user) throws SQLException;

    @Transactional
    ExecutionStatus updateMusicBand(MusicBand band, Pair<String, String> user) throws SQLException;

    ExecutionStatus loadCollection(Stack<MusicBand> collection);

}
