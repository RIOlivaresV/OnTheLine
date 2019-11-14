package com.example.projectmenu.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Item;

import java.util.ArrayList;

public class ItemDAO {

    public static final String TABLE_NAME= "Item";
    private DataBaseManager dbm;

    public ItemDAO(DataBaseManager db){this.dbm = db;}

    public long onInsert(Item item){
        SQLiteDatabase db = dbm.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(item.getID()!=0){values.put("id", item.getID());}
        values.put("name", item.getName());
        return db.insert(TABLE_NAME, null, values);
    }

    public ArrayList<Item> onSelectListItem(){
        SQLiteDatabase db = dbm.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Item> list = new ArrayList<Item>();
        while(res.moveToNext()){
            list.add(new Item(Integer.parseInt(res.getString(0)), res.getString(1)));
        }

        return list;
    }

    public int onDelete(int id){
        SQLiteDatabase db = dbm.getWritableDatabase();
        String[] arg ={String.format(String.valueOf(id))};
        return db.delete(TABLE_NAME, "id = ?", arg );
    }
}
