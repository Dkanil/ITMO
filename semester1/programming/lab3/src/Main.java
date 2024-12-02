import enums.*;
import entities.*;
import entities.human.*;
import entities.people.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        MainCharacter Gena = new MainCharacter("Гена", Location.DESERT, Stat.LONELINESS, 23);
        Gena.pray();
        Location start_location = Gena.getLocation();
        Gena.move(Location.COAST);

        ArrayList<Item> CapItems = new ArrayList<>();
        CapItems.add(Item.TUKES);
        CapItems.add(Item.FOOD);
        Capitan cap = new Capitan("Джек Воробей", CapItems, Location.COAST, Stat.HAPPINESS, 52);
        Sailors matrosses = new Sailors("Матросы", Location.COAST, Stat.HAPPINESS, 12);
        System.out.printf("%s встречает %s вместе с его %s\n", Gena.getName(), cap.getName(), matrosses.getName());

        if (start_location == Location.DESERT) {
            Gena.speak(cap.getName() + ", я так устал блуждать по пустыне, я так голоден и хочу добраться до дома, прошу Вас помочь мне");
            cap.giveItem(Gena, Item.FOOD);
        } else {
            Gena.speak(cap.getName() + ", я так хочу добраться до дома, прошу Вас помочь мне");
        }

        ArrayList<Entity> passengers = new ArrayList<>();
        passengers.add(cap);
        passengers.add(Gena);
        passengers.add(matrosses);
        Transport MainShip = new Transport("Корабль");
        MainShip.move(Location.ISLAND, passengers);
        if (passengers.get(0).getLocation() == Location.ISLAND) {
            Enemies aborigens = new Enemies("Аборигены", Location.SEA, Stat.ANGRY, 12);
            if (aborigens.AttackTransport(MainShip)){
                Gena.setStat(Stat.ANGRY);
                cap.setStat(Stat.ANGRY);
                matrosses.setStat(Stat.ANGRY);
                Transport TradingShip = new Transport("Торговый корабль");
                System.out.println("Спустя долгое время героям удалось встретить проходящий мимо " + TradingShip.name());
                TradingShip.move(Location.PORT, passengers);
            }
            else{
                MainShip.move(Location.PORT, passengers);
            }
        }
        else {
            MainShip.move(Location.PORT, passengers);
        }

        Governor governor = new Governor("Губернатор", Location.ISLAND, Stat.HAPPINESS, 68);
        cap.MakeCommand(cap, Item.TUKES, governor, matrosses);

        Gena.speak("Груза было настолько много, что казалось, будто я вовсе не собираюсь уезжать с капитаном, а остаюсь на острове до конца моих дней");
    }
}