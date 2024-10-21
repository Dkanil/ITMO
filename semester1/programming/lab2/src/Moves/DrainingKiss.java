package Moves;

import ru.ifmo.se.pokemon.*;

public class DrainingKiss extends SpecialMove {
    public DrainingKiss() {
        super(Type.FAIRY, 50, 1);
    }

    @Override
    protected void applySelfDamage(Pokemon def, double damage){
        def.setMod(Stat.HP, (int) Math.round(-0.75 * damage));
    }

    @Override
    protected String describe(){
        return "использует атаку Draining Kiss: высасывает 75% нанесённого урона на своё исцеление";
    }
}
