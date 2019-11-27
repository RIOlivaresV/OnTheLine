package com.example.projectmenu.Entities.ProceduresEntities;

public class PrepListCountPerWeek {
    private String ItemName;
    private int Count;

    public PrepListCountPerWeek(String itemName, int count) {
        ItemName = itemName;
        Count = count;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    @Override
    public String toString() {
        return "- " + ItemName +"\u0009\u0009\u0009" + Count;
    }
}
