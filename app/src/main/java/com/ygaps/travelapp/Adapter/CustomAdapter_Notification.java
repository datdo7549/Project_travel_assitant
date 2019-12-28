package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.Model.Notification;
import com.ygaps.travelapp.R;

import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter_Notification extends ArrayAdapter<Notification> {
    private Context context;
    private int resource;
    private ArrayList<Notification> arrayList;

    public CustomAdapter_Notification(@NonNull Context context, int resource, @NonNull ArrayList<Notification> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
    }

    public class ViewHolder {
        TextView message;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_item_notification, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.message=convertView.findViewById(R.id.message_noti);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.message.setText(arrayList.get(position).getMessage());
        return convertView;
    }
}
