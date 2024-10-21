import Pokemons.*;
import ru.ifmo.se.pokemon.*;

public class Main {
    public static void main(String[] args) {
        Battle b = new Battle();
        TapuBulu p1 = new TapuBulu("Абоба", 6);
        Teddiursa p2 = new Teddiursa("Хищник", 1);
        Ursaring p3 = new Ursaring("Гений", 2);
        Tynamo p4 = new Tynamo("Рыба", 3);
        Eelektrik p5 = new Eelektrik("Угорь", 4);
        Eelektross p6 = new Eelektross("Угорь с ногами", 5);

        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);

        b.go();
    }
}