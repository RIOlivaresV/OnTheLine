package com.example.projectmenu.DAOs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.User;

import java.util.ArrayList;

public class UserDAO {

    public static final String TABLE_NAME = "User";
    private DataBaseManager dbm;

    public UserDAO(DataBaseManager db) {
        this.dbm = db;
    }


    public long onInsert(User user){
        SQLiteDatabase db = dbm.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", user.getID());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        return  db.insert(TABLE_NAME, null, values);
    }

    public User onSelectUser(String email){
        SQLiteDatabase db = dbm.getWritableDatabase();
        Cursor res = db.query(TABLE_NAME, null, null, null,null, null,null);
        ArrayList<User> list = new ArrayList<User>();
        while (res.moveToNext()){
            User NewUser = new User();
            NewUser.setID(Integer.parseInt(res.getString(0)));
            NewUser.setName(res.getString(1));
            NewUser.setEmail(res.getString(2));
            NewUser.setPassword(res.getString(3));
            list.add(NewUser);
        }

        for (User user: list) {
            String emailUser = user.getEmail();
            if (user.getEmail().equals(email)){
                return user;
            }
        }
        return null;
    }


}
