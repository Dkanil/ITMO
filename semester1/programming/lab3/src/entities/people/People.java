package entities.people;

import entities.Entity;
import enums.*;

import java.util.ArrayList;

abstract public class People extends Entity {
    private int amount;

    public People(String name, ArrayList<Item> items, Location location, Stat stat, int amount) {
        super(name, items, location, stat);
        this.amount = amount;
    }

    public People(String name, Item item, Location location, Stat stat, int amount) {
        super(name, item, location, stat);
        this.amount = amount;
    }

    public People(String name, Location location, Stat stat, int amount) {
        super(name, location, stat);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    @Override
    public String toString() {
        return super.toString() + ", Количество: " + getAmount();
    }
    @Override
    public void speak(String str) {
        System.out.printf("%s произнесли: %s\n", getName(), str);
    }
}
