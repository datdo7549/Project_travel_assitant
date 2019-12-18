package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ygaps.travelapp.Model.CommentResult_TourInfo;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class CustomAdapterForTourInfo_Comment extends RecyclerView.Adapter<CustomAdapterForTourInfo_Comment.HorizontalViewHolder> {
    private ArrayList<CommentResult_TourInfo> arrayList;

    public CustomAdapterForTourInfo_Comment(ArrayList<CommentResult_TourInfo> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_comment_tour_info,parent,false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
            holder.name_user_comment.setText(arrayList.get(position).getName());
            holder.comment_string.setText(arrayList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        TextView name_user_comment;
        TextView comment_string;
        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            name_user_comment=itemView.findViewById(R.id.name_user_comment);
            comment_string=itemView.findViewById(R.id.comment_list_item);
        }
    }
}
