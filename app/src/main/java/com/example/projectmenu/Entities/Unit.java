package com.example.projectmenu.Entities;

public class Unit {

    private int ID;
    private String Name;

    public Unit(int ID, String name) {
        this.ID = ID;
        Name = name;
    }

    public Unit(){
        this.setID(-1);
        this.setName(null);
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
}
