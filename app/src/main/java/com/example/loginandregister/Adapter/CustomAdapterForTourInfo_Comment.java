package com.example.loginandregister.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginandregister.Model.CommentResult_TourInfo;
import com.example.loginandregister.Model.StopPointResult_TourInfo;
import com.example.loginandregister.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class CustomAdapterForTourInfo_Comment extends ArrayAdapter<CommentResult_TourInfo> {
    private Context context;
    private int resource;
    private ArrayList<CommentResult_TourInfo> arrayList;
    public CustomAdapterForTourInfo_Comment(@NonNull Context context, int resource, @NonNull ArrayList<CommentResult_TourInfo> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
    }

    public class ViewHolder{
        private TextView tv_name;
        private TextView tv_comment;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.list_comment_tour_info,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.tv_name=convertView.findViewById(R.id.name_user_comment);
            viewHolder.tv_comment=convertView.findViewById(R.id.comment_list_item);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.tv_name.setText(arrayList.get(position).getName());
        viewHolder.tv_comment.setText(arrayList.get(position).getComment());
        return convertView;
    }
}
