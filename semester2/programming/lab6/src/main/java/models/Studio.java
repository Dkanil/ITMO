package models;

import utility.*;

import java.util.Objects;

/**
 * Класс, представляющий студию.
 */
public class Studio implements Validatable {
    private String name; // Поле не может быть null
    private String address; // Поле может быть null

    /**
     * Конструктор для создания объекта Studio.
     * @param name название студии, не может быть null
     * @param address адрес студии, может быть null
     */
    public Studio(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * Возвращает название студии.
     * @return название студии
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает адрес студии.
     * @return адрес студии
     */
    public String getAddress() {
        return address;
    }

    /**
     * Проверяет равенство текущего объекта с другим объектом.
     * @param object объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Studio studio = (Studio) object;
        return Objects.equals(name, studio.name) && Objects.equals(address, studio.address);
    }

    /**
     * Возвращает хэш-код объекта.
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    /**
     * Возвращает строковое представление объекта.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return '{' +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    /**
     * Проверяет валидность объекта.
     * @return true, если объект валиден, иначе false
     */
    @Override
    public boolean validate() {
        return name != null;
    }
}