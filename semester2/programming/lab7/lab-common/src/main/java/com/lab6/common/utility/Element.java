package com.lab6.common.utility;

/**
 * Абстрактный класс, представляющий элемент, который можно сравнивать и проверять на валидность.
 */
public abstract class Element implements Comparable<Element>, Validatable {
    /**
     * Возвращает идентификатор элемента.
     * @return идентификатор элемента
     */
    abstract public Long getID();
}
