package com.lab6.common.utility;

import com.lab6.common.models.MusicBand;

import java.io.Serial;
import java.io.Serializable;
import java.util.Stack;

/**
 * Класс, представляющий статус выполнения операции.
 */
public class ExecutionStatus implements Serializable {
    @Serial
    private static final long serialVersionUID = 13L;
    private final boolean status;
    private String message;
    private Stack<MusicBand> collection;

    /**
     * Конструктор для создания объекта ExecutionStatus.
     *
     * @param success флаг успешности выполнения операции
     * @param message сообщение о результате выполнения операции
     */
    public ExecutionStatus(boolean success, String message) {
        this.status = success;
        this.message = message;
    }

    public ExecutionStatus(boolean success, Stack<MusicBand> collection) {
        this.status = success;
        this.collection = collection;
    }

    /**
     * Проверяет, была ли операция успешной.
     *
     * @return true, если операция была успешной, иначе false
     */
    public boolean isSuccess() {
        return status;
    }


    public Stack<MusicBand> getCollection() {
        return collection;
    }

    /**
     * Возвращает сообщение о результате выполнения операции.
     *
     * @return сообщение о результате выполнения операции
     */
    public String getMessage() {
        return message;
    }
}
