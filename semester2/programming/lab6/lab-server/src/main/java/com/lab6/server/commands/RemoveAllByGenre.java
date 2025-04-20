package com.lab6.server.commands;

import com.lab6.common.utility.Command;
import com.lab6.common.utility.CommandNames;
import com.lab6.common.validators.GenreValidator;
import com.lab6.common.models.MusicGenre;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.server.managers.CollectionManager;

/**
 * Класс команды для удаления всех элементов из коллекции по заданному жанру.
 */
public class RemoveAllByGenre extends Command<GenreValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeAllByGenre.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveAllByGenre(CollectionManager collectionManager) {
        super(CommandNames.REMOVE_ALL_BY_GENRE.getName() + " genre", CommandNames.REMOVE_ALL_BY_GENRE.getDescription(), new GenreValidator());
        this.collectionManager = collectionManager;
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
