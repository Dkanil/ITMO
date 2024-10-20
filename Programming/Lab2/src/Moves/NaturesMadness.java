package Moves;

import ru.ifmo.se.pokemon.*;

public class NaturesMadness extends SpecialMove {
    public NaturesMadness() {
        super(Type.FAIRY, 0, 90);
    }
    @Override
    protected double calcBaseDamage(Pokemon att, Pokemon def) {
        return def.getHP() / 2;
    }
    @Override
    protected String describe(){
        return "использует атаку Nature's Madness: Наносит урон, равный половине здоровья противника";
    }
}
