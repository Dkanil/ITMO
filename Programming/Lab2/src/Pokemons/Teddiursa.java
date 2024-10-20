package Pokemons;

import Moves.*;
import ru.ifmo.se.pokemon.*;

public class Teddiursa extends Pokemon {
    public Teddiursa(String name, int level) {
        super(name, level);
        setType(Type.NORMAL);
        setStats(60, 80, 50, 50, 50, 40); //HP - attack - defense - special attack - special defense - speed
        setMove(new FakeTears(), new SwordsDance(), new Facade());
    }
}
