package com.example.projectmenu.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectmenu.DAOs.DishDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Dish;
import com.example.projectmenu.Entities.Item;
import com.example.projectmenu.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AdapterDishDelete extends ArrayAdapter<Dish> {

    private Context context;
    private ArrayList<Dish> list = new ArrayList<>();
    private DataBaseManager db;

    public AdapterDishDelete(@NonNull Context context, int resource, @NonNull ArrayList<Dish> objects, DataBaseManager dataBaseManager) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.db = dataBaseManager;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getNewView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getNewView(position, convertView, parent);
    }

    public View getNewView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        if (convertView== null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_dish_delete, parent, false);

            holder = new ViewHolder();
            holder.editText = convertView.findViewById(R.id.txtDishName);
            holder.linearLayout = convertView.findViewById(R.id.listItemDish);
            holder.button = convertView.findViewById(R.id.btnDeleteDish);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.editText.setText(list.get(position).getName());
        holder.editText.setVisibility(View.VISIBLE);
        holder.editText.setEnabled(false);
        holder.editText.setPadding(15, 10, 10, 10);
        for (Item i: list.get(position).getItems()) {
            TextView tv = new TextView(context);
            tv.setText(i.getName());
            tv.setPadding(30, 10, 10, 10);
            holder.linearLayout.addView(tv);
        }
        holder.button.setText("Delete "+list.get(position).getName());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Dish");
                builder.setMessage("Are you sure you want to delete "+list.get(position).getName()+" from menu?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ArrayList<Integer> dishIds = new DishDAO(db).onSelectIDbyDishName(list.get(position).getName());
                        int num= 0;
                        for (Integer i: dishIds) {
                            num = new DishDAO(db).onDelete(i);
                        }

                        if (num != dishIds.size()){
                            Snackbar make = Snackbar.make(v, "Something is wrong, try again. ", Snackbar.LENGTH_LONG);
                            make.setAction("Action", null).show();
                        }else {
                            Snackbar make = Snackbar.make(v, list.get(position).getName()+" was deleted. ", Snackbar.LENGTH_LONG);
                            make.setAction("Action", null).show();
                        }

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

        return convertView;
    }

    private class ViewHolder{
        private TextView editText;
        private LinearLayout linearLayout;
        private Button button;
    }
}
