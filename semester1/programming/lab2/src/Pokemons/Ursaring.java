package Pokemons;

import Moves.FocusBlast;

public class Ursaring extends Teddiursa {
    public Ursaring(String name, int level) {
        super(name, level);
        setStats(90, 130, 75, 75, 75, 55); //HP - attack - defense - special attack - special defense - speed
        addMove(new FocusBlast());
    }
}
