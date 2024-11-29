package entities.human;

import enums.*;

import java.util.ArrayList;

public class Governor extends Human {
    public Governor(String name, ArrayList<Item> items, Location location, Stat stat, int age) {
        super(name, items, location, stat, age);
    }

    public Governor(String name, Item item, Location location, Stat stat, int age) {
        super(name, item, location, stat, age);
    }

    public Governor(String name, Location location, Stat stat, int age) {
        super(name, location, stat, age);
    }
}
