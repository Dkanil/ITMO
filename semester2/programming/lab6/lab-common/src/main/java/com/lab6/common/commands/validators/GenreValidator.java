package com.lab6.common.commands.validators;

import com.lab6.common.models.MusicGenre;
import com.lab6.common.utility.ExecutionStatus;

/**
 * Валидатор для проверки корректности жанра музыки.
 */
public class GenreValidator extends ArgumentValidator {
    /**
     * Проверяет корректность аргумента команды.
     *
     * @param arg Аргумент команды.
     * @param name Имя команды.
     * @return Статус выполнения проверки.
     */
    @Override
    public ExecutionStatus validate(String arg, String name) {
        if (arg.isEmpty()) {
            return new ExecutionStatus(false, "У команды должен быть аргумент (genre)!\nПример корректного ввода: " + name);
        }
        try {
            MusicGenre genre = MusicGenre.valueOf(arg);
            return new ExecutionStatus(true, "Аргумент команды введен корректно.");
        } catch (IllegalArgumentException e) {
            return new ExecutionStatus(false, "Некорректное значение поля genre!\nСписок возможных значений: " + MusicGenre.list());
        }
    }
}
