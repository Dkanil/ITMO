package Moves;

import ru.ifmo.se.pokemon.*;

public class NaturesMadness extends SpecialMove {
    public NaturesMadness() {
        super(Type.FAIRY, 0, 0.9);
    }
    @Override
    protected void applyOppDamage(Pokemon def, double damage){
        if ((int) Math.round(def.getHP() / 2) > 0) {
            def.setMod(Stat.HP, (int) Math.round(def.getHP() / 2));
        }
        else {
            def.setMod(Stat.HP, 1);
        }
    }
    @Override
    protected String describe(){
        return "использует атаку Nature's Madness: Наносит урон, равный половине здоровья противника";
    }
}
