package entities;

import java.util.ArrayList;
import enums.*;

public abstract class Human extends Entity implements Owner {
    private final String name;
    private final int age;
    private Stat stat;
    private ArrayList<Item> items;
    private Location location;
    public Human(String name, int age, Stat stat, ArrayList<Item> items, Location location) {
        this.name = name;
        this.age = age;
        this.stat = stat;
        this.items = items;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Stat getStat() {
        return stat;
    }

    public void setStat(Stat stat) {
        this.stat = stat;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void move(Location location) {
        Human.this.location = location;
    }
}
