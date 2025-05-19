package com.lab7.client.utility;

import com.lab7.client.managers.Asker;
import com.lab7.common.models.MusicBand;
import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;

/**
 * Валидатор для проверки корректности элемента коллекции.
 */
public class ElementValidator {
    /**
     * Проверяет корректность введенного элемента коллекции.
     *
     * @param console Консоль для ввода/вывода.
     * @return Пара, содержащая статус выполнения проверки и элемент коллекции.
     */
    public Pair<ExecutionStatus, MusicBand> validateAsking(Console console) {
        try {
            MusicBand band = Asker.askBand(console);
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
