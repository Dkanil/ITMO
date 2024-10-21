package Moves;

import ru.ifmo.se.pokemon.*;

public class FakeTears extends StatusMove {
    public FakeTears() {
        super(Type.DARK, 0, 1);
    }
    @Override
    protected void applyOppEffects(Pokemon p) {
        p.setMod(Stat.SPECIAL_DEFENSE, -2);
    }
    @Override
    protected String describe(){
        return "использует атаку Fake Tears: Понижает специальную защиту соперника на 2";
    }
}
