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
import com.ygaps.travelapp.Model.StopPoint2;
import com.ygaps.travelapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomAdapter_Suggest_Stop_Point extends ArrayAdapter<StopPoint2> {
    private Context context;
    private int resource;
    private ArrayList<StopPoint2> arrayList;

    public CustomAdapter_Suggest_Stop_Point(@NonNull Context context, int resource, @NonNull ArrayList<StopPoint2> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.arrayList = objects;
    }

    public class ViewHolder {
        ImageView imageView;
        TextView name;
        TextView address;
        TextView type_seervice;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_item_suggest_stop_point, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.image_suggest_stop_point);
            viewHolder.name = convertView.findViewById(R.id.name_suggest_stop_point);
            viewHolder.address = convertView.findViewById(R.id.address_suggest_stop_point);
            viewHolder.type_seervice = convertView.findViewById(R.id.type_service_suggest_stop_point);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ArrayList<String> arrayListImage = new ArrayList<>();
        arrayListImage.add("https://images.unsplash.com/photo-1573913301579-8ef529408c15?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1506800075271-265b16abe1e7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1557770402-145d15ef1596?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=700&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1570192659049-c4b377b85dae?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=675&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1488267580696-6fa71c9f7515?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1515136011719-8766a8597ae1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=794&q=80");
        Random random;
        random = new Random();
        Picasso.get()
                .load(arrayListImage.get(random.nextInt(6)))
                .fit()
                .into(viewHolder.imageView);

        viewHolder.name.setText(arrayList.get(position).getName());
        viewHolder.address.setText(arrayList.get(position).getAddress());
        switch (arrayList.get(position).getServiceTypeId()) {
            case 1: {
                viewHolder.type_seervice.setText("Restaurant");
                break;
            }
            case 2: {
                viewHolder.type_seervice.setText("Hotel");
                break;
            }
            case 3: {
                viewHolder.type_seervice.setText("Rest station");
                break;
            }
            case 4: {
                viewHolder.type_seervice.setText("Other");
                break;
            }
        }
        return convertView;
    }
}
