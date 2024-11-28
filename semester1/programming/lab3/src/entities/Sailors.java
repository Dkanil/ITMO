package entities;

import enums.*;

import java.util.ArrayList;

public class Sailors extends People{

    public Sailors(String name, ArrayList<Item> items, Location location, Stat stat, int amount) {
        super(name, items, location, stat, amount);
    }

    @Override
    public void speak(String str) {
        System.out.println(str);
    }

    public void DoCommand(String command){
        speak("- Так точно, капитан!");
        System.out.printf("Матросы %s выполнили команду %s\n", getName(), command);
    }
}
