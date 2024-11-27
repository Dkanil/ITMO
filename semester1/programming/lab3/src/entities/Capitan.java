package entities;

import enums.*;

import java.util.ArrayList;

public class Capitan extends Human {
    private int amount;
    public Capitan(String name, int age, Stat stat, ArrayList<Item> items, Location location, int amount) {
        super(name, age, stat, items, location);
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public void speak(String str) {
        System.out.println("- Йо-хо-хо! " + str);
    }

    @Override
    public void move(Location location) {
        System.out.println("- Свистать всех на верх! Мы направляемся в " + location);
    }
    public void MakeCommand(String command) {
        speak("- Всем быстро " + command + "!");

    }
}
