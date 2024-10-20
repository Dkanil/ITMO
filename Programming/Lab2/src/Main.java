import Pokemons.*;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        TapuBulu p1 = new TapuBulu("Абоба", 2);
        Teddiursa p2 = new Teddiursa("Хищник", 1);
        Ursaring p3 = new Ursaring("Гений", 3);

        b.addAlly(p1);
        b.addFoe(p2);
        b.addFoe(p3);

        b.go();
    }
}