package entities;

import java.util.ArrayList;
import enums.*;

public abstract class Human extends Entity{
    private final int age;

    public Human(String name, ArrayList<Item> items, Location location, Stat stat, int age) {
        super(name, items, location, stat);
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
