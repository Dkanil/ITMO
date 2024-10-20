package Moves;

import ru.ifmo.se.pokemon.*;

public class DrainingKiss extends SpecialMove {
    public DrainingKiss() {
        super(Type.FAIRY, 50, 100);
    }
    /*
    protected double calcBaseDamage(Pokemon att, Pokemon def) {
        return def.getHP() / 2;
    }
    */
    @Override
    protected String describe(){
        return "использует атаку Draining Kiss: Забирает 75% полученного урона на своё исцеление";
    }
}
