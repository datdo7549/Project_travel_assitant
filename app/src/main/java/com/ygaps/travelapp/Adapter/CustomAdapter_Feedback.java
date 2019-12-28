package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ygaps.travelapp.Model.Feedback_2;
import com.ygaps.travelapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter_Feedback extends ArrayAdapter<Feedback_2> {
    private Context context;
    private int resource;
    private ArrayList<Feedback_2> arrayList;
    public CustomAdapter_Feedback(@NonNull Context context, int resource, @NonNull ArrayList<Feedback_2> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.arrayList=objects;
    }

    public class ViewHoler{
        private TextView name_user_feedback;
        private TextView feedback_content;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHoler viewHoler;
        if (convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.custom_feedback_stop_point,parent,false);
            viewHoler=new ViewHoler();
            viewHoler.name_user_feedback=convertView.findViewById(R.id.name_user_feedback);
            viewHoler.feedback_content=convertView.findViewById(R.id.feedback_content);
            convertView.setTag(viewHoler);
        }
        else
        {
            viewHoler=(ViewHoler)convertView.getTag();
        }
        viewHoler.name_user_feedback.setText(arrayList.get(position).getName());
        viewHoler.feedback_content.setText(arrayList.get(position).getFeedback());
        return convertView;

    }
}
