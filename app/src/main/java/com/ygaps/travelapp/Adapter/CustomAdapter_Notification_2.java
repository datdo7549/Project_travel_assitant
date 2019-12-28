package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ygaps.travelapp.Model.Notification_2;
import com.ygaps.travelapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_Notification_2 extends ArrayAdapter<Notification_2> {
    private Context context;
    private int resource;
    private ArrayList<Notification_2> arrayList;
    public CustomAdapter_Notification_2(@NonNull Context context, int resource, @NonNull ArrayList<Notification_2> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
    }


    public class ViewHolder{
        TextView name_user_noti;
        TextView noti_content;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.custom_item_notification_2,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.name_user_noti=convertView.findViewById(R.id.name_user_notification);
            viewHolder.noti_content=convertView.findViewById(R.id.notification_content);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.name_user_noti.setText(arrayList.get(position).getName());
        viewHolder.noti_content.setText(arrayList.get(position).getNotification());
        return convertView;
    }
}
