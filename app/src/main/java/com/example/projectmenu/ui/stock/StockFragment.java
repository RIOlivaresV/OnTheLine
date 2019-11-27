package com.example.projectmenu.ui.stock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectmenu.Adapters.AdapterCheck;
import com.example.projectmenu.Adapters.AdapterDelete;
import com.example.projectmenu.Adapters.AdapterDishDelete;
import com.example.projectmenu.DAOs.DishDAO;
import com.example.projectmenu.DAOs.ItemDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Dish;
import com.example.projectmenu.Entities.Item;
import com.example.projectmenu.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class StockFragment extends Fragment {

    private StockViewModel notificationsViewModel;
    private DataBaseManager db ;
    private ArrayList<String> itemDishList = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(StockViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_stock, container, false);
        final LinearLayout ll =(LinearLayout) root.findViewById(R.id.DinamicLayout);


        try {
            db = new DataBaseManager(getContext());
            final Button buttonAddItem = (Button) root.findViewById(R.id.btnAddItem);
            buttonAddItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll.removeAllViews();
                    final EditText newItem = new EditText(getActivity());
                    newItem.setHint("Name of the new item");
                    newItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    final LinearLayout lH = new LinearLayout(getActivity());
                    lH.setOrientation(LinearLayout.HORIZONTAL);
                    Button btn = new Button(getActivity());
                    btn.setText("Save Item");
                    btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ImageButton ib = new ImageButton(getActivity());
                    ib.setImageResource(R.drawable.ic_close_black_24dp);
                    ib.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.addView(newItem);
                    lH.addView(btn);
                    lH.addView(ib);
                    ll.addView(lH);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (newItem.getText().toString().equals("")){
                                Snackbar make = Snackbar.make(v, "Type an item before save", Snackbar.LENGTH_LONG);
                                make.setAction("Action", null).show();
                            } else {
                                try {
                                    Item item = new Item(0, newItem.getText().toString());
                                    long itemAdded = new ItemDAO(db).onInsert(item);
                                    if (itemAdded>0){
                                        onCreateList(root, db);
                                        Snackbar make = Snackbar.make(v, itemAdded+" was saved successfully", Snackbar.LENGTH_LONG);
                                        make.setAction("Action", null).show();

                                    } else{
                                        Snackbar make = Snackbar.make(v, "Something is wrong, try again. ", Snackbar.LENGTH_LONG);
                                        make.setAction("Action", null).show();
                                    }
                                } catch (Exception ex){
                                    Snackbar make = Snackbar.make(v, ex.getMessage(), Snackbar.LENGTH_LONG);
                                    make.setAction("Action", null).show();
                                }

                            }
                        }
                    });
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ll.removeAllViews();
                        }
                    });
                }
            });
            Button buttonAddDish = (Button) root.findViewById(R.id.btnAddDish);
            buttonAddDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ll.removeAllViews();
                    final EditText newDish = new EditText(getActivity());
                    newDish.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    newDish.setHint("Name of the dish");
                    final Spinner itemList = new Spinner(getActivity());
                    final ArrayList<Item> list = new ItemDAO(db).onSelectListItem();
                    List<String> newList = new ArrayList<String>();
                    for (Item i: list) { newList.add(i.getName());}
                    final AdapterCheck listAdapter = new AdapterCheck(getActivity(), 0, newList);
                    itemList.setAdapter(listAdapter);
                    Button btn = new Button(getActivity());
                    btn.setText("Save Dish");
                    btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (newDish.equals("") && listAdapter.getListChecked().size()==0){
                                snakBarMessage("Please, type a name and add ingredients to the dish before save.");
                            } else {
                                for (String item: listAdapter.getListChecked()) {
                                    Item newItem = new ItemDAO(db).onSelectItem(-1, item);
                                    long dish = new DishDAO(db).onInsert(new Dish(-1, newDish.getText().toString(), newItem.getID()));
                                }
                                snakBarMessage(newDish.getText().toString()+" was added as dish.");
                            }
                        }
                    });
                    ImageButton ib = new ImageButton(getActivity());
                    ib.setImageResource(R.drawable.ic_close_black_24dp);
                    ib.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.addView(newDish);
                    ll.addView(itemList);
                    ll.addView(btn);
                    ll.addView(ib);
                    ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ll.removeAllViews();
                        }
                    });
                }
            });
            Switch switchDish = (Switch) root.findViewById(R.id.switchDishes);
            switchDish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    LinearLayout ll = (LinearLayout) root.findViewById(R.id.DinamicLayout);
                    ll.removeAllViews();
                    if (isChecked){
                        onCreateDishList(root, db);
                        buttonView.setText("Dishes");
                    }else{
                        onCreateList(root, db);
                        buttonView.setText("Items");
                    }
                }
            });
            switchDish.setTextSize(30);
            switchDish.setChecked(true);
            return root;
        } catch (Exception ex){
            snakBarMessage(ex.getMessage());
        }
        return root;
    }

    public void onCreateList(View oldview, DataBaseManager db){
        ArrayList<Item> list = new ItemDAO(db).onSelectListItem();
        ListView lv = (ListView) oldview.findViewById(R.id.list);
        AdapterDelete adapter = new AdapterDelete(getActivity(), 0, list, db);
        lv.setAdapter(adapter);


    }

    public void onCreateDishList(View olderview, DataBaseManager db){
        ArrayList<Dish> allDishes = new DishDAO(db).onSelectDishWithItem();
        ListView lv = olderview.findViewById(R.id.list);
        AdapterDishDelete listAdapter = new AdapterDishDelete(getActivity(), 0, allDishes, db);
        lv.setAdapter(listAdapter);
    }

    public void snakBarMessage(String message){
        Snackbar make = Snackbar.make(getView(), message, Snackbar.LENGTH_LONG);
        make.setAction("Action", null).show();
    }
}