package models;

import utility.*;

import java.util.Objects;

public class Coordinates implements Validatable {
    private double x; //Значение поля должно быть больше -980
    private Integer y; //Максимальное значение поля: 295, Поле не может быть null

    public Coordinates(double x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Coordinates that = (Coordinates) object;
        return Double.compare(x, that.x) == 0 && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "models.Coordinates = { " + x + " ; " + y + " }";
    }
    public boolean validate() {
        return x > -980 && y != null && y <= 295;
    }
}