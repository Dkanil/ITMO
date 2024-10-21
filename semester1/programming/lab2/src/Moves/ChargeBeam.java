package Moves;

import ru.ifmo.se.pokemon.*;

public class ChargeBeam extends SpecialMove {
    public ChargeBeam() {
        super(Type.ELECTRIC, 50, 0.9);
    }
    private boolean flag = false;

    @Override
    protected void applySelfEffects(Pokemon p) {
        Effect e = new Effect().chance(0.7).turns(1).stat(Stat.SPECIAL_ATTACK, +1);
        p.addEffect(e);
        if (e.success()){
            flag = true;
        }
    }
    @Override
    protected String describe(){
        if (flag) {
            return "повезло! Покемон использует атаку Charge Beam и повышает свою специальную атаку на 1";
        }
        else {
            return "использует атаку Charge Beam";
        }
    }
}
