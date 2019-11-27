package com.example.projectmenu.ui.prepList;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectmenu.Adapters.AdapterPrepList;
import com.example.projectmenu.DAOs.ItemDAO;
import com.example.projectmenu.DAOs.PrepDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Item;
import com.example.projectmenu.Entities.Prep;
import com.example.projectmenu.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

public class PrepListFragment extends Fragment {

    public SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyy");
    private PrepListViewModel dashboardViewModel;
    private DataBaseManager db;
    private long CurrentDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(PrepListViewModel.class);
        db = new DataBaseManager(getContext());
        final View root = inflater.inflate(R.layout.fragment_preplist, container, false);
        LinearLayout ll = (LinearLayout) root.findViewById(R.id.layoutCalendar);
        final TextView tv = (TextView) ll.findViewById(R.id.txtDate);
        final Date date = new Date();
        tv.setText(dateFormat.format(date));
        CurrentDate = date.getTime();


        final ArrayList<Prep> preplist= new PrepDAO(db).onSelectPrepList(new SimpleDateFormat("YYYY-MM-d").format(CurrentDate));
        showPrepList(preplist, root);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date d = new Date();
                        d.setDate(dayOfMonth);
                        d.setMonth(month);
                        d.setYear(year-1900);
                        tv.setText(dateFormat.format(d));
                        CurrentDate = d.getTime();
                        ArrayList<Prep> prepL= new PrepDAO(db).onSelectPrepList(new SimpleDateFormat("YYYY-MM-d").format(CurrentDate));
                        showPrepList(prepL, root);
                    }
                }, Integer.parseInt(new SimpleDateFormat("YYYY").format(CurrentDate)), Integer.parseInt(new SimpleDateFormat("M").format(CurrentDate))-1, Integer.parseInt(new SimpleDateFormat("d").format(CurrentDate)));
                datePickerDialog.show();
            }
        });

        return root;
    }

    public void showPrepList(ArrayList<Prep> prep, View root){
        LinearLayout lol = (LinearLayout) root.findViewById(R.id.LayoutList);
        final ListView list  = (ListView) lol.findViewById(R.id.prepListToday);
        final AdapterPrepList adapterPrepList= new AdapterPrepList(getContext(), 0, prep, db);
        list.setAdapter(adapterPrepList);
        FloatingActionButton fab = (FloatingActionButton) root.findViewById(R.id.FABAddList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i =new Intent(getActivity().getApplication(), newPrepList.class);
//                startActivity(i);
                LinearLayout ll = new LinearLayout(getContext());
                ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                LinearLayout llTop = new LinearLayout(getContext());
                llTop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                llTop.setOrientation(LinearLayout.HORIZONTAL);
                final AutoCompleteTextView addItem = new AutoCompleteTextView(getContext());
                addItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                addItem.setHint("Type the new Item");
                List<Item> items = new ItemDAO(db).onSelectListItem();
                List<String> listItems = new ArrayList<>();
                for (Item i:items) {
                    listItems.add(i.getName());
                }
                ArrayAdapter<String> newItemAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listItems);
                addItem.setAdapter(newItemAdapter);
                llTop.addView(addItem);
                ll.addView(llTop);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add new Item to Prep List");
                builder.setIcon(R.drawable.ic_kitchen_black_24dp);
                builder.setView(ll);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (adapterPrepList.containsElemt(addItem.getText().toString())){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Duplicated Item");
                            builder.setMessage(addItem.getText().toString()+" already exist in the PrepList");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog1 = builder.create();
                            dialog1.show();
                        } else{
                            Item item = new ItemDAO(db).onSelectItem(-1, addItem.getText().toString());
                            if (item.getName() == null){
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("New Item");
                                builder.setMessage(addItem.getText().toString()+" does not exist in your Stock, Do you want to add it?");
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Item tempItem = new Item(-1, addItem.getText().toString());
                                        tempItem.setID(Integer.parseInt(String.valueOf(new ItemDAO(db).onInsert(tempItem))));
                                        Prep newPrep = new Prep();
                                        newPrep.setItemName(tempItem.getName());
                                        newPrep.setID((int)new PrepDAO(db).onInsert(newPrep));
                                        adapterPrepList.addToList(newPrep);
                                        adapterPrepList.notifyDataSetChanged();
                                        Toast toast = Toast.makeText(getContext(), addItem.getText().toString()+" was added successfully", Toast.LENGTH_LONG);
                                        toast.show();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog dialog2 = builder.create();
                                dialog2.show();
                            }else{
                                Prep newPrep = new Prep();
                                newPrep.setItemName(item.getName());
                                newPrep.setID((int)new PrepDAO(db).onInsert(newPrep));
                                adapterPrepList.addToList(newPrep);
                                adapterPrepList.notifyDataSetChanged();
                            }
                        }
//                        UnitDAO dao = new UnitDAO(db);
//                        Unit u = dao.onSelectUnit(unit.getText().toString());
//                        u.setName(u.getName()== null?unit.getText().toString():u.getName());
//                        Prep prepItem = list.get(position);
//                        prepItem.setToDo(toDo.getText().toString());
//                        prepItem.setAmout(Float.parseFloat(amount.getText().toString()));
//                        if (dao.onSelectUnit(u.getName()).getName() == null){
//                            u.setID((int)dao.onInsert(u));
//                        }
//                        prepItem.setUnit(u.getID());
//                        int update = new PrepDAO(db).onUpdate(prepItem.getID(), prepItem);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }
}