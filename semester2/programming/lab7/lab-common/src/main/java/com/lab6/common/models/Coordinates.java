package com.lab6.common.models;

import com.lab6.common.utility.Validatable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, представляющий координаты.
 */
public class Coordinates implements Validatable, Serializable {
    @Serial
    private static final long serialVersionUID = 21L;
    private double x; // Значение поля должно быть больше -980
    private Integer y; // Максимальное значение поля: 295, Поле не может быть null

    /**
     * Конструктор для создания объекта Coordinates.
     *
     * @param x координата X, значение должно быть больше -980
     * @param y координата Y, значение не может быть null и должно быть меньше или равно 295
     */
    public Coordinates(double x, Integer y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Возвращает координату X.
     *
     * @return координата X
     */
    public double getX() {
        return x;
    }

    /**
     * Возвращает координату Y.
     *
     * @return координата Y
     */
    public Integer getY() {
        return y;
    }

    /**
     * Проверяет равенство текущего объекта с другим объектом.
     *
     * @param object объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinates that = (Coordinates) object;
        return Double.compare(x, that.x) == 0 && Objects.equals(y, that.y);
    }

    /**
     * Возвращает хэш-код объекта.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Возвращает строковое представление объекта.
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return '{' +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    /**
     * Проверяет валидность объекта.
     *
     * @return true, если объект валиден, иначе false
     */
    public boolean validate() {
        return x > -980 && y != null && y <= 295;
    }
}
