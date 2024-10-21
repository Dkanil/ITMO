package Moves;

import ru.ifmo.se.pokemon.*;

public class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 1);
    }

    private boolean flag = false;

    @Override
    protected void applySelfEffects(Pokemon p) {
        if ((p.getCondition() == Status.BURN) || (p.getCondition() == Status.PARALYZE) || (p.getCondition() == Status.POISON)) {
            this.power *= 2;
            flag = true;
        }
    }

    @Override
    protected String describe() {
        if (flag) {
            return "горит, отравлен или парализован. Покемон использует атаку Facade с удвоенной силой";
        }
        else {
            return "использует атаку Facade";
        }
    }
}
