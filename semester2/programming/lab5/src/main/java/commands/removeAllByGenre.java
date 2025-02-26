package commands;

import models.MusicGenre;
import utility.*;
import managers.*;

/**
 * Класс команды для удаления всех элементов из коллекции по заданному жанру.
 */
public class removeAllByGenre extends Command {
    Console console;
    CollectionManager collectionManager;

    /**
     * Конструктор команды removeAllByGenre.
     * @param console Консоль для ввода/вывода.
     * @param collectionManager Менеджер коллекции.
     */
    public removeAllByGenre(utility.Console console, managers.CollectionManager collectionManager) {
        super("remove_all_by_genre genre", "удалить из коллекции все элементы, значение поля genre которого эквивалентно заданному");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    /**
     * Выполняет команду удаления всех элементов коллекции по заданному жанру.
     * @param argument Аргумент команды, содержащий жанр.
     * @return Статус выполнения команды.
     */
    @Override
    public ExecutionStatus run(String argument) {
        if (argument.isEmpty()) {
            return new ExecutionStatus(false, "У команды remove_all_by_genre должен быть аргумент (genre)!\nПример корректного ввода: " + getName());
        }
        try {
            MusicGenre genre = MusicGenre.valueOf(argument);
            int count = collectionManager.removeAllByGenre(genre);
            return new ExecutionStatus(true, "Удалено " + count + " элементов!");
        } catch (IllegalArgumentException e) {
            return new ExecutionStatus(false, "Некорректное значение поля genre!\nСписок возможных значений: " + MusicGenre.list());
        } catch (Exception e) {
            return new ExecutionStatus(false, "Произошла ошибка при удалении элементов!");
        }
    }
}