package com.example.mylocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapterLocation extends BaseAdapter {

    private Context context;
    private ArrayList<MyLocation> list;
    private LayoutInflater inflater;
    public MainActivity ma;

    public MyAdapterLocation(Context context, ArrayList<MyLocation> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LocationHandler lh = null;
        if(view == null){
            view =inflater.inflate(R.layout.location_layout, null);
            lh = new LocationHandler();
            lh.name = view.findViewById(R.id.locationname);
            lh.location = view.findViewById(R.id.locationpoint);
            view.setTag(lh);
        }else lh =(LocationHandler) view.getTag();

        lh.name.setText(list.get(i).n);
        lh.location.setText(String.format("%.4f, %.4f",list.get(i).la, list.get(i).lo));

        return view;
    }

    public void notifyDataSetChanged() {
    }

    static class LocationHandler{
        TextView name, location;
    }
}