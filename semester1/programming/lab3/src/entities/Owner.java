package entities;

import enums.*;

public interface Owner {
    public void getItem();
    public void setNoItems();
    public void addItem(Item item);
    public void takeItem(Item item);
}
