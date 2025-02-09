package models;

import utility.*;

public class Studio implements Validatable {
    private String name; //Поле не может быть null
    private String address; //Поле может быть null

    public Studio(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public String toString() {
        return "models.Studio{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
    public boolean validate() {
        return name != null;
    }
}
