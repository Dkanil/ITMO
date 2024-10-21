package Pokemons;

import Moves.*;
import ru.ifmo.se.pokemon.*;

public class Tynamo extends Pokemon {
    public Tynamo(String name, int level) {
        super(name, level);
        setType(Type.ELECTRIC);
        setStats(35, 55, 40, 45, 40, 60); //HP - attack - defense - special attack - special defense - speed
        setMove(new ChargeBeam(), new ThunderWave());
    }
}
