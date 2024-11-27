import enums.*;
import entities.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello");
        Transport t = new Transport("Корабль");
        t.move(Location.SEA);
    }
}