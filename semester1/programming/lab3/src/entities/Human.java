package entities;

import enums.*;

public abstract class Human extends Entity implements Owner {
    private final String name;
    private final int age;
    private Stat stat;
    private Item item;
    public Human(String name, int age, Stat stat, Item item) {
        this.name = name;
        this.age = age;
        this.stat = stat;
        this.item = item;
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }


}
