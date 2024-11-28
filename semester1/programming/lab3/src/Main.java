import enums.*;
import entities.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Item> items = new ArrayList<Item>();
        MainCharacter Gena = new MainCharacter("Gena", items, Location.DESERT, Stat.LONELINESS, 52);
        Gena.pray();

        Capitan cap = new Capitan("Джек Воробей", items, Location.SEA, Stat.NORMAL, 52);
        Sailors matrosses = new Sailors("Балбесы", items, Location.SEA, Stat.NORMAL, 52);
        cap.MakeCommand("выгрузить тюки из корабля", matrosses);

        ArrayList<Entity> passengers = new ArrayList<Entity>();
        passengers.add(cap);
        passengers.add(Gena);
        passengers.add(matrosses);

        Transport t = new Transport("Корабль");
        t.move(Location.ISLAND, passengers);
    }
}