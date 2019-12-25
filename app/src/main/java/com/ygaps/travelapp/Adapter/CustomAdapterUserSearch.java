package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.ygaps.travelapp.Model.User;
import com.ygaps.travelapp.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CustomAdapterUserSearch extends ArrayAdapter<User> {
    private Context context;
    private int resource;
    private List<User> arrayList;

    public CustomAdapterUserSearch(@NonNull Context context, int resource, @NonNull List<User> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
    }

    public class ViewHolder{
        private TextView tv_name;
        private TextView tv_email;
        private ImageView img;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.custom_item_for_search_user_result,parent,false);
            viewHolder= new ViewHolder();
            viewHolder.tv_name=convertView.findViewById(R.id.name_of_member_search);
            viewHolder.tv_email=convertView.findViewById(R.id.email_search_user);
            viewHolder.img=convertView.findViewById(R.id.avatar_member_search);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }


        Picasso.get()
                .load("https://images.unsplash.com/photo-1572631382901-cf1a0a6087cb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=629&q=80")
                .transform(new CropCircleTransformation())
                .into(viewHolder.img);
        viewHolder.tv_name.setText(arrayList.get(position).getFullName());
        viewHolder.tv_email.setText(arrayList.get(position).getEmail());
        return convertView;
    }
}
