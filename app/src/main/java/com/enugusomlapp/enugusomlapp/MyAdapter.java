package com.enugusomlapp.enugusomlapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class MyAdapter extends BaseAdapter {

    private ArrayList<String> data;
    private ArrayList<String> data2;
    private Context context;

    MyAdapter(Context context, ArrayList<String> data1, ArrayList<String> data2) {
        super();
        this.data = data1;
        this.data2 = data2;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = LayoutInflater.from(context).
                inflate(R.layout.list_layout, parent, false);

        TextView text1 = rowView.findViewById(R.id.text1);
        TextView text2 = rowView.findViewById(R.id.text2);
        ImageView icon = rowView.findViewById(R.id.icon);

        text1.setText(data.get(position));
        text2.setText(data2.get(position));
        icon.setImageResource(R.mipmap.healthcareicon);

        return rowView;
    }

}
