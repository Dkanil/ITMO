import Pokemons.TapuBulu;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        TapuBulu p1 = new TapuBulu("Абоба", 1);
        //Pokemon p1 = new Pokemon("Чужой", 1);
        TapuBulu p2 = new TapuBulu("Хищник", 1);
        b.addAlly(p1);
        System.out.println(p1.getHP());
        System.out.println(p2.getHP());
        b.addFoe(p2);
        b.go();
    }
}