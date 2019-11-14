package com.example.projectmenu.Entities;

public class Prep {
    private int ID;
    private String ItemName;
    private String ToMake;
    private String Date;

    public Prep(int id, String itemName, String toMake, String date){
        setID(id);
        setItemName(itemName);
        setToMake(toMake);
        setDate(date);
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemName() {
        return ItemName;
    }

    public String getToMake() {
        return ToMake;
    }

    public void setToMake(String toMake) {
        ToMake = toMake;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }
}
