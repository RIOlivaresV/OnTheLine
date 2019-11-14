package com.example.projectmenu.Entities;

import java.util.Date;

public class Stock {
    private int ItemID;
    private Date Date;
    private int UnitID;
    private int UserID;

    public Stock(int itemId, Date date, int unit, int userID ){
        setItemID(itemId);
        setDate(date);
        setUnit(unit);
        setUserID(userID);
    }

    public void setItemID(int itemID) {
        ItemID = itemID;
    }

    public int getItemID() {
        return ItemID;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public void setUnit(int unit) {
        UnitID = unit;
    }

    public int getUnit() {
        return UnitID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getUserID() {
        return UserID;
    }
}
