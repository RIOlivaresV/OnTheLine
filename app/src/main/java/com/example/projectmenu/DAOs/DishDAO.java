package com.example.projectmenu.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Dish;
import com.example.projectmenu.Entities.Item;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class DishDAO {
     public static  final String TABLE_NAME = "Dish";
     private DataBaseManager dbm;

     public DishDAO(DataBaseManager db) {this.dbm = db;}

     public long onInsert(Dish dish){
         SQLiteDatabase db = dbm.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put("name", dish.getName());
         values.put("itemId", dish.getItemID());
         return  db.insert(TABLE_NAME, null, values);
     }

     public  ArrayList<Integer> onSelectIDbyDishName(String dishName){
         SQLiteDatabase db = dbm.getWritableDatabase();
         Cursor res = db.query(TABLE_NAME, null, "name='"+dishName+"'", null, null, null, null);
         ArrayList<Integer> dishIds = new ArrayList<>();
         while (res.moveToNext()){
             dishIds.add(Integer.parseInt(res.getString(0)));
         }
         return  dishIds;
     }

     public ArrayList<Dish> onSelectDishWithItem(){
         SQLiteDatabase db = dbm.getWritableDatabase();
         Cursor res = db.query(TABLE_NAME, null, null, null, null, null, null);
         ArrayList<Dish> dishList = new ArrayList<>();
         while (res.moveToNext()){
             Dish newDish = new Dish(Integer.parseInt(res.getString(0)), res.getString(1), Integer.parseInt(res.getString(0)));
             dishList.add(newDish);
         }
         ArrayList<Dish> dishListWithItems = new ArrayList<>();
         ArrayList<String> verificator = new ArrayList<>();
         for (Dish d:dishList) {
             if (!verificator.contains(d.getName())){
                 verificator.add(d.getName());
                 ArrayList<Item> itemArrayList = new ArrayList<>();
                 for (Dish dd:dishList){
                     if (dd.getName().equals(d.getName())){
                         Item i = new ItemDAO(dbm).onSelectItembyId(dd.getItemID());
                         itemArrayList.add(i);
                     }
                 }
                 d.setItems(itemArrayList);
                 dishListWithItems.add(d);
             }
         }
         return dishListWithItems;
     }

     public int onDelete(int id){
         SQLiteDatabase db = dbm.getWritableDatabase();
         String[] arg = {String.format("%", id)};
         return db.delete(TABLE_NAME, "id = ", arg);
     }
}
