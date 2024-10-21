package Moves;

import ru.ifmo.se.pokemon.*;

public class AcidSpray extends SpecialMove {
    public AcidSpray() {
        super(Type.POISON, 40, 1);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.SPECIAL_DEFENSE, -2);
    }
    @Override
    protected String describe() {
        return "использует атаку Acid Spray и понижает специальную защиту противника на 2";
    }
}
