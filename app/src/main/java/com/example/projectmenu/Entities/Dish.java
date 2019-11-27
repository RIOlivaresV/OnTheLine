package com.example.projectmenu.Entities;

import java.util.ArrayList;
import java.util.Dictionary;

public class Dish {
    private int ID;
    private String Name;
    private int ItemID;
    private ArrayList<Item> items;

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public Dish() {
        this.ID = -1;
        Name = null;
        ItemID = -1;
        this.items = new ArrayList<>();
    }

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
