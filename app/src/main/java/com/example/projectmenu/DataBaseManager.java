package com.example.projectmenu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseManager extends SQLiteOpenHelper {
    public DataBaseManager(Context context){
        super(context, DBDefinition.DATADABE_NAME, null, 1 );
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            db.execSQL("PRAGMA foreign_key=ON");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User (id integer primary key autoincrement, name text, email text, password text )");
        db.execSQL("CREATE TABLE Item (id integer primary key autoincrement, name text)");
        db.execSQL("CREATE TABLE Dish (id integer primary key autoincrement, name text, itemId integer, foreign key(itemId) references Item(id))");
        db.execSQL("CREATE TABLE Unit (id integer primary key autoincrement, name text)");
        db.execSQL("CREATE TABLE Prep (id integer primary key autoincrement, itemName text, toDo text, amount real, unit integer, date text, foreign key(unit) references Unit(id))");
        db.execSQL("CREATE TABLE Stock (itemId integer, date text, unitId integer, userId integer, foreign key(itemId) references Item(id), foreign key (unitId) references Unit(id), foreign key(userId) references User(id)  )");
        Log.d("DataBaseManager", "onCreate");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Item");
        db.execSQL("DROP TABLE IF EXISTS Dish");
        db.execSQL("DROP TABLE IF EXISTS Unit");
        db.execSQL("DROP TABLE IF EXISTS Stock");
        onCreate(db);
    }
}
