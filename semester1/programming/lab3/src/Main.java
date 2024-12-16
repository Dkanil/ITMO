import enums.*;
import entities.*;
import entities.human.*;
import entities.people.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MainCharacter Gena = new MainCharacter("Гена", Location.DESERT, Stat.LONELINESS, 23);

        ArrayList<Item> CapItems = new ArrayList<>();
        CapItems.add(Item.TUKES);
        CapItems.add(Item.FOOD);
        Capitan cap = new Capitan("Джек Воробей", CapItems, Location.COAST, Stat.HAPPINESS, 52);

        Sailors matrosses = new Sailors("Матросы", Location.COAST, Stat.HAPPINESS, 12);
        Enemies aborigens = new Enemies("Аборигены", Location.SEA, Stat.ANGRY, 12);
        aborigens.setSmart(false);

        Governor governor = new Governor("Губернатор", Location.ISLAND, Stat.HAPPINESS, 68);

        Story.tellStory(Gena, cap, matrosses, aborigens, governor);
    }
}