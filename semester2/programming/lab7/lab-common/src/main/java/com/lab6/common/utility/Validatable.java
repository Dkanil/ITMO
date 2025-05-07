package com.lab6.common.utility;

/**
 * Интерфейс для объектов, которые могут быть проверены на валидность.
 */
public interface Validatable {
    /**
     * Проверяет валидность объекта.
     * @return true, если объект валиден, иначе false.
     */
    boolean validate();
}
