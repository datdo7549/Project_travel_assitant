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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.Model.Member;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CustomAdapterForTourInfo_Member extends RecyclerView.Adapter<CustomAdapterForTourInfo_Member.HorizontalViewHolder> {

    private ArrayList<Member> arrayList;

    public CustomAdapterForTourInfo_Member(ArrayList<Member> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_member_tour_info,parent,false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, int position) {
        holder.name_of_member.setText(arrayList.get(position).getName());
        Picasso.get()
                .load("https://images.unsplash.com/photo-1572631382901-cf1a0a6087cb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=629&q=80")
                .transform(new CropCircleTransformation())
                .into(holder.avatar_member);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        ImageView avatar_member;
        TextView name_of_member;
        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar_member=itemView.findViewById(R.id.avatar_member);
            name_of_member=itemView.findViewById(R.id.name_of_member);
        }
    }

}
