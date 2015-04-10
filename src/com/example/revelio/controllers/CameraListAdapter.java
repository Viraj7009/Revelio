package com.example.revelio.controllers;



import java.util.ArrayList;
import java.util.List;

import com.example.revelio.R;




import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class CameraListAdapter extends BaseAdapter{

private LayoutInflater myInflater;
private List<String> list;

public CameraListAdapter(LayoutInflater inflater){ //(Context context) {
    myInflater =inflater; //LayoutInflater.from(context);

}

public void setData(List<String> list) {
    this.list = list;
}

@Override
public int getCount() {
    return list.size();
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

    ViewHolder holder;        
    
    convertView     = myInflater.inflate(R.layout.eventslist_adapter, null);
    holder          = new ViewHolder();
    //holder.id  
    //TextView id= (TextView) convertView.findViewById(R.id.textid);
    //id.setTextColor(Color.WHITE);
    //holder.name 
    TextView name= (TextView) convertView.findViewById(R.id.textname);
    name.setTextColor(Color.WHITE);
    convertView.setTag(holder);

    //Weather weather = data[position];
    //events td=new events();//list.get(position);//[position];
    
    //id.setText(""+td.getEventId()+":");
    //id.setText(position+1+":");
    name.setText(position+1+"."+list.get(position));
    //name.setText(td.getEventName());   
    //list.add(td);
    		return convertView;	
}

static class ViewHolder {
    TextView id;
    TextView name ;
}

}



