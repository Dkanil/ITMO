package utility;

import models.MusicBand;

/**
 * Класс для представления пары значений ExecutionStatus и MusicBand.
 */
public class Pair {
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
