package entities;

import enums.*;

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

    public Entity(String name, Item item, Location location, Stat stat) {
        this.name = name;
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);
        this.items = items;
        this.location = location;
        this.stat = stat;
    }

    public Entity(String name, Location location, Stat stat) {
        this.name = name;
        this.items = new ArrayList<>();
        this.location = location;
        this.stat = stat;
    }

    public String getStat() {
        return stat.getTitle();
    }

    public void setStat(Stat stat) {
        System.out.printf("**%s теперь испытывает %s**\n", getName(), stat.getTitle());
        this.stat = stat;
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

    @Override
    public void setNoItems() {
        this.items = new ArrayList<>();
        System.out.printf("**У %s больше нет вещей**\n", getName());
    }
    @Override
    public void addItem(Item item) {
        this.items.add(item);
        System.out.printf("**%s получил %s**\n", getName(), item.getTitle());
    }
    @Override
    public void takeItem(Item item) {
        this.items.remove(item);
        System.out.printf("**%s утратил %s**\n", getName(), item.getTitle());
    }
    @Override
    public void getItem() {
        System.out.printf("**У %s есть", getName());
        String output = "";
        for (Item item : items) {
            output += item.getTitle() + ", ";
        }
        output = output.substring(0, output.length() - 2);
        System.out.println(output + "**");
    }

    abstract public void speak(String str);
    public void move(Location location) {
        setLocation(location);
        System.out.printf("**%s теперь в %s**\n", getName(), location.getTitle());
    }
}
