package com.example.projectmenu.ui.home;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import com.example.projectmenu.Adapters.AdapterPrepList;
import com.example.projectmenu.DAOs.DishDAO;
import com.example.projectmenu.DAOs.ItemDAO;
import com.example.projectmenu.DAOs.PrepDAO;
import com.example.projectmenu.DAOs.ProceduresDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Dish;
import com.example.projectmenu.Entities.Item;
import com.example.projectmenu.Entities.Prep;
import com.example.projectmenu.Entities.ProceduresEntities.PrepListCountPerWeek;
import com.example.projectmenu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private DataBaseManager db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        db = new DataBaseManager(getContext());

        LinearLayout ll = root.findViewById(R.id.prepListView);
        ArrayList<Prep> preplisHome = new PrepDAO(db).onSelectPrepList(new SimpleDateFormat("YYYY-MM-d").format(new Date()));
        TextView tvTitle = new TextView(getContext());
        tvTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setText("Prep List Today");
        tvTitle.setTextSize(30);
        tvTitle.setPadding(0, 20, 0, 20);
        ll.addView(tvTitle);
        for (Prep p:preplisHome) {
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tv.setPadding(30, 10, 10, 10);
            tv.setTextSize(20);
            tv.setText(p.getItemName()+" - "+p.getToDo());
            ll.addView(tv);
        }

        LinearLayout llCount = root.findViewById(R.id.listCountPerWeek);
        ArrayList<PrepListCountPerWeek> list = new ProceduresDAO(db).onSelectCountPrepPerWeek();
        TextView tvTitle2 = new TextView(getContext());
        tvTitle2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tvTitle2.setGravity(Gravity.CENTER);
        tvTitle2.setText("Count Prep \nList per weeks");
        tvTitle2.setTextSize(25);
        tvTitle2.setPadding(0, 20, 0, 20);
        llCount.addView(tvTitle2);
        for (PrepListCountPerWeek ppw:list) {
            TextView tv = new TextView(getContext());
            tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tv.setPadding(30, 10, 10, 10);
            tv.setTextSize(20);
            tv.setText(ppw.toString());
            llCount.addView(tv);
        }







        return root;
    }
}