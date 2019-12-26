package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ygaps.travelapp.Model.CommentResult_TourInfo;
import com.ygaps.travelapp.Model.Feedback;
import com.ygaps.travelapp.Model.Rating_result;

import com.ygaps.travelapp.Model.onCLickRecycleView_Rating;
import com.ygaps.travelapp.R;

import java.util.ArrayList;

public class CustomAdapterForTourInfo_Rating extends RecyclerView.Adapter<CustomAdapterForTourInfo_Rating.HorizontalViewHolder> {
    private ArrayList<Feedback> arrayList;

    onCLickRecycleView_Rating onClickRecycleViewRating;
    public CustomAdapterForTourInfo_Rating(ArrayList<Feedback> arrayList,onCLickRecycleView_Rating onClickRecycleViewRating) {
        this.arrayList = arrayList;
        this.onClickRecycleViewRating=onClickRecycleViewRating;
    }

    @NonNull
    @Override
    public HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.list_rating_tour_info,parent,false);
        return new HorizontalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalViewHolder holder, final int position) {
        holder.name_user_rating.setText(arrayList.get(position).getName());
        holder.feedback_string.setText(arrayList.get(position).getReview());
        holder.send_report_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRecycleViewRating.rating(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        TextView name_user_rating;
        TextView feedback_string;
        ImageView send_report_rating;
        public HorizontalViewHolder(@NonNull View itemView) {
            super(itemView);
            name_user_rating=itemView.findViewById(R.id.name_user_rating);
            feedback_string=itemView.findViewById(R.id.feedback_list_item);
            send_report_rating=itemView.findViewById(R.id.send_report_rating);
        }
    }
}
