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
import com.squareup.picasso.Transformation;
import com.ygaps.travelapp.R;
import com.ygaps.travelapp.Model.Stop_Point;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class CustomAdapterForListStopPoint extends ArrayAdapter<Stop_Point> {
    private Context context;
    private int resource;
    private ArrayList<Stop_Point> arrayList;

    public CustomAdapterForListStopPoint(@NonNull Context context, int resource, @NonNull ArrayList<Stop_Point> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
    }
    public class ViewHolder{
        private TextView tv;
        private ImageView img;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.list_stop_point_tour_info,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.tv=convertView.findViewById(R.id.stop_point_name_list_item);
            viewHolder.img=convertView.findViewById(R.id.image_stop_point_tour_info);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        final Transformation transformation = new RoundedCornersTransformation(55,5);
        Picasso.get()
                .load("https://cdn.pixabay.com/photo/2017/04/04/23/36/ben-thanh-market-2203445_960_720.jpg")
                .transform(transformation)
                .fit()
                .into(viewHolder.img);
        viewHolder.tv.setText(arrayList.get(position).getName());
        return convertView;
    }
}
