package Moves;

import ru.ifmo.se.pokemon.*;

public class RockTomb extends PhysicalMove {
    public RockTomb() {
        super(Type.ROCK, 60, 0.95);
    }
    private boolean flag = false;
    @Override
    protected void applyOppEffects(Pokemon p) {
        Effect e = new Effect().chance(1).stat(Stat.SPEED, -1);
        p.addEffect(e);
        if (e.success()){
            flag = true;
        }
    }
    @Override
    protected String describe(){
        if (flag) {
            return "повезло! Покемон использует атаку Rock Tomb и понижает скорость соперника на 1";
        }
        else {
            return "использует атаку Rock Tomb";
        }
    }
}