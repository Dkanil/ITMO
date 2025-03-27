package commands.validators;

import managers.Asker;
import models.MusicBand;
import utility.*;

/**
 * Валидатор для проверки корректности элемента коллекции.
 */
public class ElementValidator {
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
            if (band != null && band.validate()) {
                return new Pair<>(new ExecutionStatus(true, "Элемент введён корректно!"), band);
            }
            return new Pair<>(new ExecutionStatus(false, "Введены некорректные данные!"), null);
        } catch (Asker.Breaker e) {
            return new Pair<>(new ExecutionStatus(false, "Ввод был прерван пользователем!"), null);
        } catch (Asker.IllegalInputException e) {
            return new Pair<>(new ExecutionStatus(false, e.getMessage()), null);
        }
    }
}