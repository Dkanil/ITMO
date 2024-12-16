package entities;

import enums.*;
import exceptions.*;

import java.util.ArrayList;
import java.util.Objects;

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

    protected void setLocation(Location location) {
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
    public void addItem(Item item, boolean flag) {
        this.items.add(item);
        if (flag) {
            System.out.printf("**%s получил %s**\n", getName(), item.getTitle());
        }
    }
    @Override
    public void takeItem(Item item, boolean flag) {
        this.items.remove(item);
        if (flag) {
            System.out.printf("**%s утратил %s**\n", getName(), item.getTitle());
        }
    }
    @Override
    public void giveItem(Entity to, Item item) throws NoItems {
        if (getItem(false).contains(item)) {
            this.items.remove(item);
            to.addItem(item, false);
            System.out.printf("**%s передал %s к %s**\n", getName(), item.getTitle(), to.getName());
        }
        else {
            throw new NoItems("У " + getName() + " отсутствует " + item.getTitle());
        }
    }
    @Override
    public ArrayList<Item> getItem(boolean flag) {
        if (flag) {
            if (items.isEmpty()) {
                System.out.printf("**У %s нет предметов**\n", getName());
            }
            else{
                System.out.printf("**У %s есть", getName());
                String output = "";
                for (Item item : items) {
                    output += item.getTitle() + ", ";
                }
                output = output.substring(0, output.length() - 2);
                System.out.println(output + "**");
            }
        }
        return items;
    }

    public void meet(Object object) {
        System.out.printf("%s встречает %s\n", getName(), object.toString());
    }
    abstract public void speak(String str);
    public void move(Location location) {
        if (!getLocation().equals(location)) {
            setLocation(location);
            System.out.printf("**%s теперь в %s**\n", getName(), location.getTitle());
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
