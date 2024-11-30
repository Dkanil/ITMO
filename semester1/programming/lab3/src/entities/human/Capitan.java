package entities.human;

import entities.*;
import entities.people.*;
import enums.*;
import exceptions.*;

import java.util.ArrayList;

public class Capitan extends Human {

    public Capitan(String name, ArrayList<Item> items, Location location, Stat stat, int age) {
        super(name, items, location, stat, age);
    }

    public Capitan(String name, Item item, Location location, Stat stat, int age) {
        super(name, item, location, stat, age);
    }

    public Capitan(String name, Location location, Stat stat, int age) {
        super(name, location, stat, age);
    }

    @Override
    public void speak(String str) {
        System.out.printf("Капитан %s произнёс: Йо-хо-хо! %s\n", getName(), str);
    }

    public void MakeCommand(Entity from, Item item, Entity to, Sailors sailors) {
        if (from.equals(this)) {
            speak(String.format("%s, выполнить команду передать мои %s к %s!", sailors.getName(), item.getTitle(), to.getName()));
        }
        else {
            speak(String.format("%s, выполнить команду передать %s от %s к %s!", sailors.getName(), item.getTitle(), from.getName(), to.getName()));
        }
        try {
            sailors.DoCommand(from, item, to);
        } catch (NoItems e) {
            System.out.println(e.getMessage());
            speak("Отставить приказ!");
        }
    }
    @Override
    public String toString() {
        return "Капитан " + super.toString();
    }
}
