package com.example.projectmenu.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectmenu.DAOs.ItemDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Item;
import com.example.projectmenu.R;
import com.example.projectmenu.ui.stock.StockFragment;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AdapterDelete extends ArrayAdapter<Item> {
    private Context context;
    private ArrayList<Item> list = new ArrayList<>();
    private DataBaseManager db;

    public AdapterDelete(@NonNull Context context, int resource, ArrayList<Item> list, DataBaseManager dataBaseManager) {
        super(context, resource, list);
        this.context = context;
        this.list = list;
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

    public View getNewView(final int position, View convertView, final ViewGroup parent){
        final ViewHolder holder;
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_item_delete, parent, false);

            holder= new ViewHolder();
            holder.textView = convertView.findViewById(R.id.itemList);
            holder.linearLayout = convertView.findViewById(R.id.btnLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final int listPos = position-1;
        if (position<1){
            holder.textView.setText("Tap to delete");
            holder.textView.setVisibility(View.VISIBLE);
        } else {
            holder.textView.setVisibility(View.VISIBLE);
            holder.textView.setText(list.get(listPos).getName());

            holder.textView.setTag(listPos);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int chil = holder.linearLayout.getChildCount();
                    if (chil == 0) {
                        Button btnDelete = new Button(context);
                        btnDelete.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        btnDelete.setText("Delete " + list.get(listPos).getName());
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Delete item")
                                        .setMessage("Are you sure you want to delete " + list.get(listPos).getName() + " from your items?")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                int num = new ItemDAO(db).onDelete(list.get(listPos).getID());
                                                if (num == 0) {
                                                    Snackbar make = Snackbar.make(v, "Something is wrong, try again. ", Snackbar.LENGTH_LONG);
                                                    make.setAction("Action", null).show();
                                                } else {
                                                    Snackbar make = Snackbar.make(v, list.get(listPos).getName() + " was deleted from your stock", Snackbar.LENGTH_LONG);
                                                    make.setAction("Action", null).show();
                                                    list.remove(listPos);
                                                    notifyDataSetChanged();
                                                    holder.linearLayout.removeAllViews();
                                                }
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });

                        holder.linearLayout.addView(btnDelete);
                    } else {
                        holder.linearLayout.removeAllViews();
                    }

                }
            });
        }
        return convertView;
    }

    private class ViewHolder{
        private TextView textView;
        private LinearLayout linearLayout;
    }
}
