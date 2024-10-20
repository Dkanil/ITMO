package Moves;

import ru.ifmo.se.pokemon.*;

public class FocusBlast extends SpecialMove {
    public FocusBlast() {
        super(Type.FIGHTING, 120, 0.7);
    }
    private boolean flag = false;
    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() < 0.1) {
            p.setMod(Stat.SPECIAL_DEFENSE, -1);
            flag = true;
        }
    }
    @Override
    protected String describe(){
        if (flag) {
            return "повезло! Покемон использует атаку Focus Blast и понижает специальную защиту соперника на 1";
        }
        else {
            return "использует атаку Focus Blast";
        }
    }
}
