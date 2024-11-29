package entities.human;

import java.util.ArrayList;

import entities.Entity;
import enums.*;

public abstract class Human extends Entity {
    private final int age;

    public Human(String name, ArrayList<Item> items, Location location, Stat stat, int age) {
        super(name, items, location, stat);
        this.age = age;
    }

    public Human(String name, Item item, Location location, Stat stat, int age) {
        super(name, item, location, stat);
        this.age = age;
    }

    public Human(String name, Location location, Stat stat, int age) {
        super(name, location, stat);
        this.age = age;
    }

    public int getAge() {
        return age;
    }
    @Override
    public void speak(String str) {
        System.out.printf("%s произнёс: %s\n", getName(), str);
    }
}
