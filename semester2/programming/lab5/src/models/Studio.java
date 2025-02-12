package models;

import utility.*;

import java.util.Objects;

public class Studio implements Validatable {
    private String name; //Поле не может быть null
    private String address; //Поле может быть null

    public Studio(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Studio studio = (Studio) object;
        return Objects.equals(name, studio.name) && Objects.equals(address, studio.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
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
