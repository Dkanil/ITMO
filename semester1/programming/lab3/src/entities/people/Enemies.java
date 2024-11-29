package entities.people;

import enums.Item;
import enums.Location;
import enums.Stat;

import java.util.ArrayList;

public class Enemies extends People{
    public Enemies(String name, ArrayList<Item> items, Location location, Stat stat, int amount) {
        super(name, items, location, stat, amount);
    }

    public Enemies(String name, Item item, Location location, Stat stat, int amount) {
        super(name, item, location, stat, amount);
    }

    public Enemies(String name, Location location, Stat stat, int amount) {
        super(name, location, stat, amount);
    }

    @Override
    public void speak(String str) {
        System.out.printf("%s промычали: У-А-Ы-УНГА-БУНГА\n", getName());
    }

}
