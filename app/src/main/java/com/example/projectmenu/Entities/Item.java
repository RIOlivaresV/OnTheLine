package com.example.projectmenu.Entities;

public class Item {
    private int ID;
    private String Name;

    public Item(int id, String name){
        setID(id);
        setName(name);
    }
    public Item(){
        setID(-1);
        setName(null);
    }

    public int getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
