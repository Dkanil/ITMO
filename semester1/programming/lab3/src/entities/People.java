package entities;

import enums.Item;
import enums.Location;
import enums.Stat;

import java.util.ArrayList;

abstract public class People extends Entity {
    private int amount;

    public People(String name, ArrayList<Item> items, Location location, Stat stat, int amount) {
        super(name, items, location, stat);
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
