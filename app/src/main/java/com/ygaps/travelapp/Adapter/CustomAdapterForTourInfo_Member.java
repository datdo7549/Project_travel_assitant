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

import com.ygaps.travelapp.Model.Member;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class CustomAdapterForTourInfo_Member extends ArrayAdapter<Member> {
    private Context context;
    private int resource;
    private ArrayList<Member> arrayList;
    public CustomAdapterForTourInfo_Member(@NonNull Context context, int resource, @NonNull ArrayList<Member> objects) {
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
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.list_member_tour_info,parent,false);
            convertView.setClipToOutline(true);
            viewHolder.tv=convertView.findViewById(R.id.name_member_list_item);
            viewHolder.img=convertView.findViewById(R.id.avatar_member_tour_info);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        //https://image.flaticon.com/icons/svg/145/145842.svg
        //https://cdn.pixabay.com/photo/2017/04/04/23/36/ben-thanh-market-2203445_960_720.jpg
        /*
        final Transformation transformation = new RoundedCornersTransformation(55,5);
        Picasso.get()
                .load("https://image.flaticon.com/icons/svg/145/145842.svg")
                .into(viewHolder.img);


         */
        viewHolder.tv.setText(arrayList.get(position).getName());
        viewHolder.img.setImageResource(R.drawable.avatar_man);
        convertView.setClipToOutline(true);
        return convertView;
    }
}
