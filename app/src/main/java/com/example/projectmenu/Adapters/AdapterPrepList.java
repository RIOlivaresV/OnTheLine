package com.example.projectmenu.Adapters;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.print.PrintAttributes;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.projectmenu.DAOs.ItemDAO;
import com.example.projectmenu.DAOs.PrepDAO;
import com.example.projectmenu.DAOs.UnitDAO;
import com.example.projectmenu.DataBaseManager;
import com.example.projectmenu.Entities.Item;
import com.example.projectmenu.Entities.Prep;
import com.example.projectmenu.Entities.Unit;
import com.example.projectmenu.R;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AdapterPrepList extends ArrayAdapter<Prep> {
    private Context context;
    private ArrayList<Prep> list = new ArrayList<>();
    private DataBaseManager db;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyy");

    public AdapterPrepList(@NonNull Context context, int resource, @NotNull ArrayList<Prep> objects, DataBaseManager dataBaseManager){
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.db = dataBaseManager;
    }

    public void addToList(Prep prep){
        this.list.add(prep);
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
            convertView = layoutInflater.inflate(R.layout.list_preplist, parent, false);

            holder = new ViewHolder();
            holder.editText = convertView.findViewById(R.id.txtItemAdded);
            List<Item> items = new ItemDAO(db).onSelectListItem();
            List<String> listItems = new ArrayList<>();
            for (Item i:items) {
                listItems.add(i.getName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listItems);
            holder.editText.setAdapter(adapter);
            holder.linearLayoutTop = convertView.findViewById(R.id.LayoutTop);
            holder.linearLayoutBottom = convertView.findViewById(R.id.LayoutBottom);
            convertView.setTag(holder);
        } else {
            holder  = (ViewHolder) convertView.getTag();
        }


        holder.editText.setText(list.get(position).getItemName());
        holder.editText.setVisibility(View.VISIBLE);
        holder.editText.setFocusable(false);
        holder.editText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                LinearLayout ll = new LinearLayout(context);
                ll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ll.setOrientation(LinearLayout.VERTICAL);
                LinearLayout llTop = new LinearLayout(context);
                llTop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                llTop.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout llBottom = new LinearLayout(context);
                llBottom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                llBottom.setOrientation(LinearLayout.HORIZONTAL);
                llBottom.setWeightSum(2);
                final EditText toDo = new EditText(getContext());
                toDo.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                toDo.setText(list.get(position).getToDo()== null?"":list.get(position).getToDo());
                toDo.setHint("What to do?");
                llTop.addView(toDo);
                final EditText amount = new EditText(getContext());
                amount.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                amount.setText(list.get(position).getAmout()== -1?"":String.valueOf(list.get(position).getAmout()));
                amount.setHint("Amount");
                final EditText unit = new EditText(getContext());
                unit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                Unit units = new UnitDAO(db).onSelectUnit(list.get(position).getUnit(), null);
                //change UnitDAO onSelectUnit, it needs to add another attribute on the sign with the ID
                //set name on the unit and set the id in the list
                unit.setText(list.get(position).getUnit()==-1?"":units.getName());
                unit.setHint("Unit");
                llBottom.addView(amount);
                llBottom.addView(unit);
                ll.addView(llTop);
                ll.addView(llBottom);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(holder.editText.getText().toString()+"'s details.");
                builder.setIcon(R.drawable.ic_info_white_24dp);
                builder.setView(ll);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UnitDAO dao = new UnitDAO(db);
                        Unit u = dao.onSelectUnit(-1, unit.getText().toString());
                        u.setName(u.getName()== null?unit.getText().toString():u.getName());
                        Prep prepItem = list.get(position);
                        prepItem.setToDo(toDo.getText().toString());
                        prepItem.setAmout(Float.parseFloat(amount.getText().toString()));
                        if (dao.onSelectUnit(-1, u.getName()).getName() == null){
                            u.setID((int)dao.onInsert(u));
                        }
                        prepItem.setUnit(u.getID());
                        int update = new PrepDAO(db).onUpdate(prepItem.getID(), prepItem);
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
        private AutoCompleteTextView editText;
        private LinearLayout linearLayoutTop;
        private LinearLayout linearLayoutBottom;
    }

    public boolean containsElemt(String element){
        boolean exist = false;
        for (Prep p:list) {
            exist = p.getItemName().equals(element);
            if (exist == true) return exist;
        }
        return exist;
    }

}
