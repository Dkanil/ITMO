package entities;

import enums.*;

import java.util.ArrayList;

public interface Owner {
    ArrayList<Item> getItem(boolean flag);
    void setNoItems();
    void addItem(Item item, boolean flag);
    void takeItem(Item item,  boolean flag);
    void giveItem(Entity to, Item item);
}
