package entities;

import enums.Item;
import enums.Location;
import enums.Stat;

import java.util.ArrayList;

public class MainCharacter extends Human{
    public MainCharacter(String name, ArrayList<Item> items, Location location, Stat stat, int age) {
        super(name, items, location, stat, age);
    }

    @Override
    public void speak(String str) {
        System.out.println(str);
    }

    public void pray() {
        speak("Я так устал испытывать " + getStat() + ", прошу, избавь меня от этого чувства");
        if (Math.random() < 0.7) {
            speak("Всевышний услышал мои молитвы и избавил меня от " + getStat());
            setStat(Stat.NORMAL);
        }
        else {
            System.out.println("Молитвы героя не были услышаны и герой скончался от " + getStat());
            setStat(Stat.DEATH);
        }
    }
}
