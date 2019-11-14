package com.example.projectmenu.DAOs;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Dish;

public class DishDAO {
     public static  final String TABLE_NAME = "Dish";
     private DataBaseManager dbm;

     public DishDAO(DataBaseManager db) {this.dbm = db;}

     public long onInsert(Dish dish){
         SQLiteDatabase db = dbm.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put("id", dish.getID());
         values.put("name", dish.getName());
         values.put("itemId", dish.getItemID());
         return  db.insert(TABLE_NAME, null, values);
     }

     public int onDelete(int id){
         SQLiteDatabase db = dbm.getWritableDatabase();
         String[] arg = {String.format("%", id)};
         return db.delete(TABLE_NAME, "id = ", arg);
     }
}
