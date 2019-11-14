package com.example.projectmenu.ui.stock;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectmenu.AdapterCheck;
import com.example.projectmenu.AdapterDelete;
import com.example.projectmenu.DAOs.ItemDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Item;
import com.example.projectmenu.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.TRANSPARENT;

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
                                        Snackbar make = Snackbar.make(v, itemAdded+" was saved successfully", Snackbar.LENGTH_LONG);
                                        make.setAction("Action", null).show();
                                        onCreateList(root, db);

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
                    EditText newDish = new EditText(getActivity());
                    newDish.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    newDish.setHint("Name of the dish");
                    Spinner itemList = new Spinner(getActivity());
                    final ArrayList<Item> list = new ItemDAO(db).onSelectListItem();
                    List<String> newList = new ArrayList<String>();
                    for (Item i: list) { newList.add(i.getName());}
                    AdapterCheck listAdapter = new AdapterCheck(getActivity(), 0, newList);
                    itemList.setAdapter(listAdapter);
//                    itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            onCreateDishList(root, position, list);
//                        }
//                    });
                    Button btn = new Button(getActivity());
                    btn.setText("Save Dish");
                    btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.addView(newDish);
                    ll.addView(itemList);
                    ll.addView(btn);
                }
            });
            onCreateList(root, db);
            return root;
        } catch (Exception ex){
            Snackbar make = Snackbar.make(getView(), ex.getMessage(), Snackbar.LENGTH_LONG);
            make.setAction("Action", null).show();
        }
        return root;
    }

    public void onCreateList(View oldview, DataBaseManager db){
        ArrayList<Item> list = new ItemDAO(db).onSelectListItem();
        ListView lv = oldview.findViewById(R.id.list);
        AdapterDelete adapter = new AdapterDelete(getActivity(), 0, list, db);
        lv.setAdapter(adapter);

    }

    public void onCreateDishList(View olderview, int position, ArrayList<Item> list){
        itemDishList.add(list.get(position).getName());
        ListView lv = olderview.findViewById(R.id.list);
        AdapterCheck listAdapter = new AdapterCheck(getActivity(), 0, itemDishList);
        lv.setAdapter(listAdapter);
    }
}