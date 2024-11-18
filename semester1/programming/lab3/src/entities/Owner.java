package entities;

import enums.*;

public interface Owner {
    public default void getItem(String name, Item[] items){
        for(Item item : items){
            System.out.println("У " + name + " есть " + item);
        }
    }
}
