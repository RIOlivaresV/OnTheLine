package com.example.projectmenu.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Unit;

import java.util.ArrayList;

public class UnitDAO {
    public static final String TABLE_NAME = "Unit";
    private DataBaseManager dbm;

    public  UnitDAO(DataBaseManager db){ this.dbm = db;}

    public long onInsert(Unit unit){
        SQLiteDatabase db = dbm.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", unit.getName());
        return  db.insert(TABLE_NAME, null, values);
    }

    public Unit onSelectUnit(int id, String name){
        SQLiteDatabase db = dbm.getWritableDatabase();
        if (id>0){
            Cursor res = db.query(TABLE_NAME, null, "id="+id, null, null, null, null);
            Unit unitFound = new Unit();
            while (res.moveToNext()){
                unitFound.setID(Integer.parseInt(res.getString(0)));
                unitFound.setName(res.getString(1));
            }
            return  unitFound;
        } else if (name!=null){
            Cursor res = db.query(TABLE_NAME, null, "name = '"+name+"'", null, null, null, null);
            Unit unitFound = new Unit();
            while (res.moveToNext()){
                unitFound.setID(Integer.parseInt(res.getString(0)));
                unitFound.setName(res.getString(1));
            }
            return  unitFound;
        }

        return new Unit();
    }

    public ArrayList<Unit> onSelectListUnit(){
        SQLiteDatabase db = dbm.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Unit> UnitList = new ArrayList<>();
        while (res.moveToNext()){
            Unit u = new Unit(Integer.parseInt(res.getString(0)), res.getString(1));
            UnitList.add(u);
        }
        return UnitList;
    }

    public int onDelete(int id){
        SQLiteDatabase db = dbm.getWritableDatabase();
        String[] arg = {String.valueOf(id)};
        return db.delete(TABLE_NAME, "id=?", arg);
    }
}
