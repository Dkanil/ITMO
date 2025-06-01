package com.lab8.server.commands;

import com.lab8.common.utility.Pair;
import com.lab8.server.utility.Command;
import com.lab8.server.utility.CommandNames;
import com.lab8.common.validators.GenreValidator;
import com.lab8.common.models.MusicGenre;
import com.lab8.common.utility.ExecutionStatus;

/**
 * Класс команды для удаления всех элементов из коллекции по заданному жанру.
 */
public class RemoveAllByGenre extends Command<GenreValidator> {

    /**
     * Конструктор команды removeAllByGenre.
     */
    public RemoveAllByGenre() {
        super(CommandNames.REMOVE_ALL_BY_GENRE.getName() + " genre", CommandNames.REMOVE_ALL_BY_GENRE.getDescription(), new GenreValidator());
    }

    /**
     * Выполняет команду удаления всех элементов коллекции по заданному жанру.
     * @param argument Аргумент команды, содержащий жанр.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        MusicGenre genre = MusicGenre.valueOf(argument);
        return collectionManager.removeAllByGenre(genre, user);
    }
}
