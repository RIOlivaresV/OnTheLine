package com.example.projectmenu.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Prep;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PrepDAO {
    public static final String TABLE_NAME = "Prep";
    private DataBaseManager dbm;

    public PrepDAO(DataBaseManager db){this.dbm = db;}

    public long onInsert(Prep prep){
        SQLiteDatabase db = dbm.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("itemName", prep.getItemName());
        values.put("toDo", prep.getToDo());
        values.put("amount", prep.getAmout());
        values.put("unit", prep.getUnit());
        values.put("date", prep.getDate());
        return  db.insert(TABLE_NAME, null, values);
    }

    public int onUpdate(int id, Prep prep){
        SQLiteDatabase db = dbm.getWritableDatabase();
        String[] arg = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put("toDo", prep.getToDo());
        values.put("amount", String.valueOf(prep.getAmout()));
        values.put("unit", String.valueOf(prep.getUnit()));
        return  db.update(TABLE_NAME, values, "id=?", arg );
    }

    public int onInsertList(ArrayList<Prep> prepList){
        int counter= 0;
        for (Prep prep:prepList) {
            counter += onInsert(prep) == -1 ? 0:1;
        }
        return counter;
    }

    public Prep onSelectPrep(String date, String itemName){
       SQLiteDatabase db = dbm.getWritableDatabase();
       String[] arg = {itemName.equals("")?"":itemName, date.equals("")?"":date};
       Cursor res = db.query(TABLE_NAME, null, "itemName=? and date=?", arg, null, null, null);
       Prep prepFound = new Prep();
       while (res.moveToNext()){
           prepFound.setID(Integer.parseInt(res.getString(0)));
           prepFound.setItemName(res.getString(1));
           prepFound.setToDo(res.getString(2));
           prepFound.setAmout(Float.parseFloat(res.getString(3)));
           prepFound.setUnit(Integer.parseInt(res.getString(4)));
           prepFound.setDate(res.getString(5));
       }
       return  prepFound;
    }

    public  ArrayList<Prep> onSelectPrepList(String date){
        SQLiteDatabase db = dbm.getWritableDatabase();
        String[] arg = {date.equals("")?"":date};
        Cursor res = db.query(TABLE_NAME, null, "date=?", arg, null, null, null);
        ArrayList<Prep> prepList = new ArrayList<>();
        while (res.moveToNext()){
            Prep prepFound = new Prep();
            prepFound.setID(Integer.parseInt(res.getString(0)));
            prepFound.setItemName(res.getString(1));
            prepFound.setToDo(res.getString(2));
            prepFound.setAmout(Float.parseFloat(res.getString(3)));
            prepFound.setUnit(Integer.parseInt(res.getString(4)));
            prepFound.setDate(res.getString(5));
            prepList.add(prepFound);
        }
        return  prepList;
    }

    public int onDelete(int id){
        SQLiteDatabase db = dbm.getWritableDatabase();
        String[] arg = {String.valueOf(id)};
        return  db.delete(TABLE_NAME, "id=?", arg);
    }
}
