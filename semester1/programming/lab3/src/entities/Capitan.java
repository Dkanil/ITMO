package entities;

import enums.*;

import java.util.ArrayList;

public class Capitan extends Human {

    public Capitan(String name, ArrayList<Item> items, Location location, Stat stat, int age) {
        super(name, items, location, stat, age);
    }

    @Override
    public void speak(String str) {
        System.out.println("- Йо-хо-хо! " + str);
    }

    @Override
    public void move(Location location) {
        setLocation(location);
        System.out.println("- Свистать всех на верх! Мы направляемся в " + location);
    }
    public void MakeCommand(String command, Sailors sailors) {
        speak(sailors.getName() + ", выполнить команду "+ command + "!");
        sailors.DoCommand(command);
    }
}
