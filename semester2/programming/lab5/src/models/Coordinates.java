package models;

import utility.*;

public class Coordinates implements Validatable {
    private double x; //Значение поля должно быть больше -980
    private Integer y; //Максимальное значение поля: 295, Поле не может быть null

    public Coordinates(double x, Integer y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "models.Coordinates = { " + x + " ; " + y + " }";
    }
    public boolean validate() {
        return x > -980 && y != null && y <= 295;
    }
}