package com.example.projectmenu.Entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

public class Prep {
    private int ID;
    private String ItemName;
    private String ToDo;
    private float Amout;
    private int Unit;
    private String Date;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-d");

    public Prep(int id, String itemName, String toDo, String date, float amount, int unit){
        setID(id);
        setItemName(itemName);
        setToDo(toDo);
        setDate(date);
        setAmout(amount);
        setUnit(unit);
    }

    public Prep(){
        setID(-1);
        setItemName(null);
        setToDo(null);
        setAmout(-1);
        setUnit(-1);
        setDate(dateFormat.format(new Date()));
    }

    public double getAmout() {
        return Amout;
    }

    public void setAmout(float amout) {
        Amout = amout;
    }

    public int getUnit() {
        return Unit;
    }

    public void setUnit(int unit) {
        Unit = unit;
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

    public String getToDo() {
        return ToDo;
    }

    public void setToDo(String toMake) {
        ToDo = toMake;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDate() {
        return Date;
    }
}
