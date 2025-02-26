package commands;

import models.MusicGenre;
import utility.*;
import managers.*;

/**
 * Класс команды для удаления всех элементов из коллекции по заданному жанру.
 */
public class RemoveAllByGenre extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeAllByGenre.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public RemoveAllByGenre(utility.Console console, managers.CollectionManager collectionManager) {
        super(CommandNames.REMOVE_ALL_BY_GENRE.getName() + " genre", CommandNames.REMOVE_ALL_BY_GENRE.getDescription());
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Проверяет корректность аргументов команды.
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        if (arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды должен быть аргумент (genre)!\nПример корректного ввода: " + getName());
        }
        try {
            MusicGenre genre = MusicGenre.valueOf(arg);
            return new ExecutionStatus(true, "Аргумент команды введен корректно.");
        } catch (IllegalArgumentException e) {
            return new ExecutionStatus(false, "Некорректное значение поля genre!\nСписок возможных значений: " + MusicGenre.list());
        }
    }

    /**
     * Выполняет команду удаления всех элементов коллекции по заданному жанру.
     * @param argument Аргумент команды, содержащий жанр.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        ExecutionStatus argumentStatus = validate(argument, getName());
        if (argumentStatus.isSuccess()) {
            MusicGenre genre = MusicGenre.valueOf(argument);
            int count = collectionManager.removeAllByGenre(genre);
            return new ExecutionStatus(true, "Удалено " + count + " элементов!");
        } else {
            return argumentStatus;
        }
    }
}