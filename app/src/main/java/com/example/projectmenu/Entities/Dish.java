package com.example.projectmenu.Entities;

public class Dish {
    private int ID;
    private String Name;
    private int ItemID;

    public Dish(int id, String name, int itemId){
        setID(id);
        setName(name);
        setItemID(itemId);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getItemID() {
        return ItemID;
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }
}
