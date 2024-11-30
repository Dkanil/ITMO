package entities;

import enums.*;

import java.util.ArrayList;

public record Transport(String name) {

    public void move(Location location, ArrayList<Entity> passengers){
        System.out.printf("Благодаря %s:\n", name);
        for (Entity passenger : passengers) {
            passenger.move(location);
        }
    }
}
