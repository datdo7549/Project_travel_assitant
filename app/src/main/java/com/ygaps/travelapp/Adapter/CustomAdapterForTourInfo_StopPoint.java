package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ygaps.travelapp.Model.StopPointResult_TourInfo;
import com.ygaps.travelapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class CustomAdapterForTourInfo_StopPoint extends ArrayAdapter<StopPointResult_TourInfo> {
    private Context context;
    private int resource;
    private ArrayList<StopPointResult_TourInfo> arrayList;
    public CustomAdapterForTourInfo_StopPoint(@NonNull Context context, int resource, @NonNull ArrayList<StopPointResult_TourInfo> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
    }

    public class ViewHolder{
        private TextView tv;
        private ImageView img;
        private LinearLayout edit_delete;
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
            viewHolder.edit_delete=convertView.findViewById(R.id.edit_delete_stop_point);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }

        final Transformation transformation = new RoundedCornersTransformation(45,5);
        Picasso.get()
                .load("https://cdn.pixabay.com/photo/2017/04/04/23/36/ben-thanh-market-2203445_960_720.jpg")
                .transform(transformation)
                .fit()
                .into(viewHolder.img);
        viewHolder.tv.setText(arrayList.get(position).getName());


        if(arrayList.get(position).getCheck()==0)
        {
            viewHolder.edit_delete.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.edit_delete.setVisibility(View.VISIBLE);
        }
        return convertView;
    }
}
