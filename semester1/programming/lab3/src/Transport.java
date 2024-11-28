import entities.Entity;
import enums.Location;

import java.util.ArrayList;

public record Transport(String name) {
    public void move(Location location, ArrayList<Entity> passengers){
        System.out.printf("Благодаря %s, ", name);
        String output = "";
        for (Entity passenger : passengers) {
            output += passenger.getName() + ", ";

            passenger.setLocation(location);
        }
        output = output.substring(0, output.length() - 2);
        System.out.println(output + " успешно добираются к " + location.getTitle());
    }
}
