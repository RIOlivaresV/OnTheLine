package com.example.projectmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AdapterCheck extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> list= new ArrayList<>();
    public ArrayList<String> listChecked = new ArrayList<>();
    public AdapterCheck(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list.addAll(objects);
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
            convertView = layoutInflater.inflate(R.layout.list_item_check, parent, false);

            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            holder.textView = convertView.findViewById(R.id.ItemCheck);
            convertView.setTag(holder);
        } else{
            holder = (ViewHolder) convertView.getTag();
        }

        final int listPos = position-1;
        if (position<1){
            holder.checkBox.setVisibility(View.GONE);
            holder.textView.setText("Select All items for this dish");
        } else {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.textView.setText(list.get(listPos));

            holder.checkBox.setTag(listPos);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                }
            });
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        listChecked.add(list.get(listPos));
                    }
                }
            });
        }

        return convertView;
    }

    private class ViewHolder{
        private CheckBox checkBox;
        private TextView textView;
    }
}
