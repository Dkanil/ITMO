package com.lab6.server.commands;

import com.lab6.server.utility.Command;
import com.lab6.server.utility.CommandNames;
import com.lab6.common.validators.GenreValidator;
import com.lab6.common.models.MusicGenre;
import com.lab6.common.utility.ExecutionStatus;

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
    public ExecutionStatus runInternal(String argument) {
        MusicGenre genre = MusicGenre.valueOf(argument);
        int count = collectionManager.removeAllByGenre(genre);
        return new ExecutionStatus(true, "Удалено " + count + " элементов!");
    }
}
