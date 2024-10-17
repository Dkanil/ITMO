package Moves.SpecialMoves;

import ru.ifmo.se.pokemon.*;

public class DrainingKiss extends SpecialMove {
    public DrainingKiss() {
        super(Type.NORMAL, 0, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.EVASION, -1);
    }
    @Override
    protected String describe(){
        return "использует атаку Draining Kiss: Понижает уклонение соперника на 1";
    }
}
