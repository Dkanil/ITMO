package commands;

import commands.validators.GenreValidator;
import models.MusicGenre;
import utility.*;
import managers.*;

/**
 * Класс команды для удаления всех элементов из коллекции по заданному жанру.
 */
public class RemoveAllByGenre extends Command<GenreValidator> {
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeAllByGenre.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveAllByGenre(Console console, managers.CollectionManager collectionManager) {
        super(CommandNames.REMOVE_ALL_BY_GENRE.getName() + " genre", CommandNames.REMOVE_ALL_BY_GENRE.getDescription(), console, new GenreValidator());
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