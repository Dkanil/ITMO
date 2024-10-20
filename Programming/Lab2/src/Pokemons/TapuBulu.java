package Pokemons;

import Moves.*;
import ru.ifmo.se.pokemon.*;

public class TapuBulu extends Pokemon {
    public TapuBulu(String name, int level) {
        super(name, level);
        setType(Type.GRASS, Type.FAIRY);
        setStats(70, 130, 115, 85, 95, 75); //HP - attack - defense - special attack - special defense - speed
        setMove(new Flatter(), new DrainingKiss(), new NaturesMadness(), new SweetScent());
    }
}
