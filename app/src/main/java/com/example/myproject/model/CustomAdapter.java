package com.example.myproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myproject.R;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {
    Context context;
//    ArrayList<String> cusName;
    ArrayList<Customer> cusData;

    public CustomAdapter(Context context,ArrayList<Customer> cusData) {
        this.context = context;
        this.cusData = cusData;
    }



    public int getCount(){
        return cusData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
             convertView = inflater.inflate(R.layout.list_item,parent,false);

        TextView textView1 = convertView.findViewById(R.id.text1);
        textView1.setText(cusData.get(position).name());

        TextView textView2 = convertView.findViewById(R.id.text2);
        textView2.setText(cusData.get(position).address());

        return convertView;
    }





}
