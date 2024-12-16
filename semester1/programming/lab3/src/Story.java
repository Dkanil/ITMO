import entities.*;
import entities.human.*;
import entities.people.*;
import enums.*;

import java.util.ArrayList;

public class Story {
    public static void tellStory(MainCharacter mainCharacter, Capitan capitan, Sailors sailors, Enemies enemies, Governor governor) {
        mainCharacter.pray();
        Location start_location = mainCharacter.getLocation();
        mainCharacter.move(Location.COAST);

        mainCharacter.meet(capitan);
        mainCharacter.meet(sailors);

        if (start_location == Location.DESERT) {
            mainCharacter.speak(capitan.getName() + ", я так устал блуждать по пустыне, я так голоден и хочу добраться до дома, прошу Вас помочь мне");
            capitan.giveItem(mainCharacter, Item.FOOD);
        } else {
            mainCharacter.speak(capitan.getName() + ", я так хочу добраться до дома, прошу Вас помочь мне");
        }

        ArrayList<Entity> passengers = new ArrayList<>();
        passengers.add(capitan);
        passengers.add(mainCharacter);
        passengers.add(sailors);

        Transport MainShip = new Transport("Корабль");
        MainShip.move(Location.ISLAND, passengers);
        if (passengers.get(0).getLocation() == Location.ISLAND) {
            if (enemies.AttackTransport(MainShip)) {
                mainCharacter.setStat(Stat.ANGRY);
                capitan.setStat(Stat.ANGRY);
                sailors.setStat(Stat.ANGRY);
                Transport TradingShip = new Transport("Торговый корабль");
                capitan.meet(TradingShip.name());
                sailors.meet(TradingShip.name());
                mainCharacter.meet(TradingShip.name());
                TradingShip.move(Location.PORT, passengers);
            } else {
                MainShip.move(Location.PORT, passengers);
            }
        } else {
            MainShip.move(Location.PORT, passengers);
        }
        capitan.MakeCommand(capitan, Item.TUKES, governor, sailors);
        mainCharacter.speak("Груза было настолько много, что казалось, будто я вовсе не собираюсь уезжать с капитаном, а остаюсь на острове до конца моих дней");
    }
}
