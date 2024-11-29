package entities;

import enums.*;

import java.util.ArrayList;

public interface Owner {
    public ArrayList<Item> getItem(boolean flag);
    public void setNoItems();
    public void addItem(Item item);
    public void takeItem(Item item);
}
