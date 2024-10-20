package Moves;

import ru.ifmo.se.pokemon.*;

public class Flatter extends StatusMove{
    public Flatter() {
        super(Type.DARK, 0, 100);
    }
    @Override
    protected void applyOppEffects(Pokemon p){
        p.setMod(Stat.SPECIAL_ATTACK, +1);
        p.confuse();
    }
    @Override
    protected String describe(){
        return "использует атаку Flatter: Повышает специальную атаку соперника на 1 и вызывает у него растерянность";
    }
}