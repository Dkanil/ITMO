package entities;

import enums.Location;

public abstract class Entity {
    abstract public void speak(String str);
    abstract public void move(Location location);
    public Entity() {
    }
}
