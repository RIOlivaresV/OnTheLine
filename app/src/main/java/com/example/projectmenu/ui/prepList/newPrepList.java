package com.example.projectmenu.ui.prepList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.projectmenu.Adapters.AdapterPrepList;
import com.example.projectmenu.DAOs.PrepDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Prep;
import com.example.projectmenu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.String.format;

public class newPrepList extends AppCompatActivity {
    private DataBaseManager db;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_prep_list);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        db = new DataBaseManager(this);

        ImageButton ib = (ImageButton) findViewById(R.id.btnClose);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        ArrayList<Prep> preplist= new PrepDAO(db).onSelectPrepList(dateFormat.format(new Date()));
        preplist.add(0, new Prep(-1,"","", new Date().toString(), 0, -1 ));
        ListView lv = (ListView) findViewById(R.id.prepListToday);
        AdapterPrepList adapterPrepList= new AdapterPrepList(this, 0, preplist, db);
        lv.setAdapter(adapterPrepList);
    }
}
