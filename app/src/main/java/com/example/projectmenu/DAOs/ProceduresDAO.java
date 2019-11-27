package com.example.projectmenu.DAOs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.ProceduresEntities.PrepListCountPerWeek;

import java.util.ArrayList;

public class ProceduresDAO {
    private DataBaseManager dbm;

    public  ProceduresDAO(DataBaseManager db){ this.dbm = db;}

    public ArrayList<PrepListCountPerWeek> onSelectCountPrepPerWeek(){
        SQLiteDatabase db = dbm.getWritableDatabase();
        Cursor res = db.rawQuery("select distinct itemName, count(amount) as Count from Prep where STRFTIME('%W', date) = STRFTIME('%W', date('now')) group by itemName", null);
        ArrayList<PrepListCountPerWeek> list = new ArrayList<>();
        while (res.moveToNext()){
            list.add(new PrepListCountPerWeek(res.getString(0), Integer.parseInt(res.getString(1))));
        }
        return  list;
    }
}
