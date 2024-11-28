package entities;

import enums.Item;
import enums.Location;
import enums.Stat;

import java.util.ArrayList;

public abstract class Entity implements Owner {
    private final String name;
    private ArrayList<Item> items;
    private Location location;
    private Stat stat;

    public Entity(String name, ArrayList<Item> items, Location location, Stat stat) {
        this.name = name;
        this.items = items;
        this.location = location;
        this.stat = stat;
    }

    public String getStat() {
        return stat.getTitle();
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

    public String getName() {
        return name;
    }

    abstract public void speak(String str);
    public void move(Location location) {
        setLocation(location);
        System.out.println(getName() + " переместился в " + location);
    }
}
