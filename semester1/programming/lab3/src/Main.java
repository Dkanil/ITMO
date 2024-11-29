import enums.*;
import entities.*;
import entities.human.*;
import entities.people.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MainCharacter Gena = new MainCharacter("Гена", Location.DESERT, Stat.LONELINESS, 23);
        Gena.pray();

        Capitan cap = new Capitan("Джек Воробей", Item.TUKES, Location.SEA, Stat.HAPPINESS, 52);
        Sailors matrosses = new Sailors("Матросы", Location.SEA, Stat.HAPPINESS, 12);
        Governor governor = new Governor("Губернатор", Location.ISLAND, Stat.HAPPINESS, 68);

        cap.MakeCommand(cap, Item.TUKES, governor, matrosses);

        Enemies aborigens = new Enemies("Аборигены", Location.SEA, Stat.HAPPINESS, 12);
        ArrayList<Entity> passengers = new ArrayList<>();
        passengers.add(cap);
        passengers.add(Gena);
        passengers.add(matrosses);
        aborigens.speak("СЕЙЧАС МЫ ВАС СЪЕДИМ");
        Transport t = new Transport("Корабль");
        t.move(Location.ISLAND, passengers);
    }
}