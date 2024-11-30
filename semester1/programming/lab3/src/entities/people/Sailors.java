package entities.people;

import entities.*;
import enums.*;
import exceptions.*;

import java.util.ArrayList;

public class Sailors extends People {

    public Sailors(String name, ArrayList<Item> items, Location location, Stat stat, int amount) {
        super(name, items, location, stat, amount);
    }

    public Sailors(String name, Item item, Location location, Stat stat, int amount) {
        super(name, item, location, stat, amount);
    }

    public Sailors(String name, Location location, Stat stat, int amount) {
        super(name, location, stat, amount);
    }

    public void DoCommand(Entity from, Item item, Entity to) throws NoItems {
        speak("Так точно, капитан!");
        if (from.getItem(false).contains(item)) {
            from.giveItem(this, item);
            this.giveItem(to, item);
            speak("Приказ успешно выполнен, капитан!");
        }
        else {
            throw new NoItems("У " + from.getName() + " отсутствует " + item.getTitle());
        }
    }
}
