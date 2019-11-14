package com.example.projectmenu.DAOs;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Stock;

public class StockDAO {
    public static  final String TABLE_NAME = "Stock";
    private DataBaseManager dbm;

    public StockDAO(DataBaseManager dbm) {
        this.dbm = dbm;
    }

    public long onInsert(Stock stock){
        SQLiteDatabase db = dbm.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("itemId", stock.getItemID());
        values.put("date", stock.getDate().toString());
        values.put("unitId", stock.getUnit());
        values.put("userId", stock.getUserID());
        return  db.insert(TABLE_NAME, null, values);
    }

    public int onDelete(int id){
        SQLiteDatabase db = dbm.getWritableDatabase();
        String[] arg = {String.format("%", id)};
        return db.delete(TABLE_NAME, "id=", arg);
    }
}
