package Moves.StatusMoves;

import ru.ifmo.se.pokemon.*;

public class SweetScent extends StatusMove {
    public SweetScent() {
        super(Type.NORMAL, 0, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.EVASION, -1);
    }
    @Override
    protected String describe(){
        return "использует атаку Sweet Scent: Понижает уклонение соперника на 1";
    }
}