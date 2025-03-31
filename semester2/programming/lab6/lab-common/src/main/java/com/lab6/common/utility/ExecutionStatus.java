package com.lab6.common.utility;

/**
 * Класс, представляющий статус выполнения операции.
 */
public class ExecutionStatus {
    private Pair<Boolean, String> status;

    /**
     * Конструктор для создания объекта ExecutionStatus.
     *
     * @param success флаг успешности выполнения операции
     * @param message сообщение о результате выполнения операции
     */
    public ExecutionStatus(boolean success, String message) {
        this.status = new Pair<>(success, message);
    }

    /**
     * Проверяет, была ли операция успешной.
     *
     * @return true, если операция была успешной, иначе false
     */
    public boolean isSuccess() {
        return status.getFirst();
    }

    /**
     * Возвращает сообщение о результате выполнения операции.
     *
     * @return сообщение о результате выполнения операции
     */
    public String getMessage() {
        return status.getSecond();
    }
}
