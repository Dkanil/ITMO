package entities;

import enums.*;

import java.util.ArrayList;

public class Sailors extends Human{

    public Sailors(String name, int age, Stat stat, ArrayList<Item> items, Location location) {
        super(name, age, stat, items, location);
    }
    @Override
    public void speak(String str) {
        System.out.println(str);
    }

    public void DoCommand(){
        speak("- Так точно, капитан!");
    }
}
