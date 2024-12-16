package entities.people;

import entities.*;
import enums.*;
import java.util.ArrayList;

public class Enemies extends People{
    private boolean smart = Math.random() * 100 > 55;
    public Enemies(String name, ArrayList<Item> items, Location location, Stat stat, int amount) {
        super(name, items, location, stat, amount);
    }

    public Enemies(String name, Item item, Location location, Stat stat, int amount) {
        super(name, item, location, stat, amount);
    }

    public Enemies(String name, Location location, Stat stat, int amount) {
        super(name, location, stat, amount);
    }

    public boolean isSmart() {
        return smart;
    }
    public void setSmart(boolean smart) {
        this.smart = smart;
    }

    @Override
    public void speak(String str) {
        if(isSmart()) {
            super.speak(str);
        }
        else {
            System.out.printf("%s промычали: У-А-Ы-УНГА-БУНГА\n", getName());
        }
    }

    public boolean AttackTransport(Transport transport) {
        System.out.printf("О нет! %s захватили %s!\n", getName(), transport.name());
        speak("СЕЙЧАС МЫ ВАС СЪЕДИМ");
        if (isSmart()) {
            ArrayList<Entity> passengers = new ArrayList<>();
            passengers.add(this);
            transport.move(Location.SEA, passengers);
            System.out.printf("Похоже %s смогли уйти вместе с %s!\n", getName(), transport.name());
            return true;
        }
        else {
            System.out.printf("%s оказались слишком глупы не поняли, как управлять %s.\n%s освобождён!\n", getName(), transport.name(), transport.name());
            return false;
        }
    }

}
