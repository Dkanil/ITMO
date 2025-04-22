package com.lab6.common.validators;

import com.lab6.common.managers.Asker;
import com.lab6.common.models.MusicBand;
import com.lab6.common.utility.Console;
import com.lab6.common.utility.ExecutionStatus;
import com.lab6.common.utility.Pair;

import java.io.Serial;
import java.io.Serializable;

/**
 * Валидатор для проверки корректности элемента коллекции.
 */
public class ElementValidator implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    /**
     * Проверяет корректность введенного элемента коллекции.
     *
     * @param console Консоль для ввода/вывода.
     * @param id Идентификатор элемента коллекции.
     * @return Пара, содержащая статус выполнения проверки и элемент коллекции.
     */
    public Pair<ExecutionStatus, MusicBand> validateAsking(Console console, Long id) {
        try {
            MusicBand band = Asker.askBand(console, id);
            return validating(band);
        } catch (Asker.Breaker e) {
            return new Pair<>(new ExecutionStatus(false, "Ввод был прерван пользователем!"), null);
        } catch (Asker.IllegalInputException e) {
            return new Pair<>(new ExecutionStatus(false, e.getMessage()), null);
        }
    }

    public Pair<ExecutionStatus, MusicBand> validating(MusicBand band) {
        if (band != null && band.validate()) {
            return new Pair<>(new ExecutionStatus(true, "Элемент введён корректно!"), band);
        }
        return new Pair<>(new ExecutionStatus(false, "Введены некорректные данные!"), null);
    }
}
