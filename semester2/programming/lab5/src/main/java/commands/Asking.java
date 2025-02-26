package commands;

import managers.*;
import models.MusicBand;
import utility.*;

/**
 * Интерфейс для команд, запрашивающих данные у пользователя.
 */
public interface Asking {

    /**
     * Класс для представления пары значений ExecutionStatus и MusicBand.
     */
    class Pair {
        private ExecutionStatus executionStatus;
        private MusicBand band;

        /**
         * Конструктор для создания пары значений.
         * @param executionStatus Статус выполнения команды.
         * @param band Музыкальная группа.
         */
        public Pair(ExecutionStatus executionStatus, MusicBand band) {
            this.executionStatus = executionStatus;
            this.band = band;
        }

        /**
         * Возвращает статус выполнения команды.
         * @return Статус выполнения команды.
         */
        public ExecutionStatus getExecutionStatus() {
            return executionStatus;
        }

        /**
         * Возвращает музыкальную группу.
         * @return Музыкальная группа.
         */
        public MusicBand getBand() {
            return band;
        }
    }

    /**
     * Проверяет корректность введенных данных и создает объект MusicBand.
     * @param console Консоль для ввода/вывода.
     * @param id Идентификатор музыкальной группы.
     * @return Пара значений ExecutionStatus и MusicBand.
     */
    default Pair validate(Console console, Long id) {
        try {
            MusicBand band = Asker.askBand(console, id);
            if (band != null && band.validate()) {
                return new Pair(new ExecutionStatus(true, "Элемент успешно добавлен в коллекцию!"), band);
            }
            return new Pair(new ExecutionStatus(false, "Введены некорректные данные!"), null);
        } catch (Asker.Breaker e) {
            return new Pair(new ExecutionStatus(false, "Ввод был прерван пользователем!"), null);
        }
    }
}