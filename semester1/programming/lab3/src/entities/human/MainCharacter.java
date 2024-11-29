package entities.human;

import enums.*;

import java.util.ArrayList;

public class MainCharacter extends Human {
    public MainCharacter(String name, ArrayList<Item> items, Location location, Stat stat, int age) {
        super(name, items, location, stat, age);
    }

    public MainCharacter(String name, Item item, Location location, Stat stat, int age) {
        super(name, item, location, stat, age);
    }

    public MainCharacter(String name, Location location, Stat stat, int age) {
        super(name, location, stat, age);
    }

    public void pray() {
        speak("Я так устал испытывать " + getStat() + ", прошу, избавь меня от этого чувства");
        if (Math.random() < 0.9) {
            speak("Всевышний услышал мои молитвы и избавил меня от " + getStat());
            setStat(Stat.HAPPINESS);

        }
        else {
            System.out.println("Молитвы героя не были услышаны и герой скончался от " + getStat());
        }
    }
}
