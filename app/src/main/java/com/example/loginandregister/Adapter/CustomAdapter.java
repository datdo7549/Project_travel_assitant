package com.example.loginandregister.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.loginandregister.Model.Tour;
import com.example.loginandregister.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<Tour> {

    private  Context context;
    private  int resource;
    private  List<Tour> tours;


    public CustomAdapter(@NonNull Context context, int resource, @NonNull List<Tour> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.tours=objects;
    }

    public static class ViewHolder
    {
        ImageView imageTour,imageName,imageDate,imageAdult,imageChild,imageCost;
        TextView name,date,adult,child,cost;
        ImageButton btnCopy;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRow=convertView;
        if(viewRow==null)
        {
            viewRow= layoutInflater.inflate(resource,parent,false);
            ViewHolder viewHolder=new ViewHolder();
            viewHolder.imageTour=viewRow.findViewById(R.id.imageTour);
            viewHolder.imageName=viewRow.findViewById(R.id.imageName);
            viewHolder.imageDate=viewRow.findViewById(R.id.imageDate);
            viewHolder.imageAdult=viewRow.findViewById(R.id.imageAdults);
            viewHolder.imageChild=viewRow.findViewById(R.id.imageChilds);
            viewHolder.imageCost=viewRow.findViewById(R.id.imageCost);
            viewHolder.name=viewRow.findViewById(R.id.name);
            viewHolder.date=viewRow.findViewById(R.id.date);
            viewHolder.adult=viewRow.findViewById(R.id.adults);
            viewHolder.child=viewRow.findViewById(R.id.childs);
            viewHolder.cost=viewRow.findViewById(R.id.min_max_Cost);
            viewHolder.btnCopy=viewRow.findViewById(R.id.btncopy);
            viewRow.setTag(viewHolder);
        }

        // if TH load view lan thu 2
        ViewHolder viewHolder= (ViewHolder) viewRow.getTag();


        // cho nay chen hinh anh string avatar  ??
        //viewHolder.imageTour.setImageResource(R.drawable.imagetour);
        Picasso.get()
                .load("https://www.w3schools.com/howto/img_snow.jpg")
                .into(viewHolder.imageTour);
        // tim hinh anh phu hop
//        viewHolder.imageName.setImageResource(R.drawable.pass);
//        viewHolder.imageDate.setImageResource(R.drawable.pass);
//        viewHolder.imageAdult.setImageResource(R.drawable.pass);
//        viewHolder.imageChild.setImageResource(R.drawable.pass);
//        viewHolder.imageCost.setImageResource(R.drawable.pass);
        // xu li tiep phan gop chuoi
        viewHolder.name.setText(tours.get(position).getName());

        // xu li start vs end Date
        long miliStartDate=Long.parseLong(tours.get(position).getStartDate());
        Date startD=new Date(miliStartDate);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        String temp1=dateFormat.format(startD);
        StringBuilder dateBuild=new StringBuilder();

        long miliEndDate=Long.parseLong(tours.get(position).getEndDate());
        Date endD=new Date(miliEndDate);
        DateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy");
        String temp2=dateFormat1.format(endD);
        dateBuild.append(temp1).append("-").append(temp2);
        viewHolder.date.setText(dateBuild.toString());

        // set adult and child
        StringBuilder adultsBuild=new StringBuilder();
        adultsBuild.append((String.valueOf(tours.get(position).getAdults()))).append(" adults");
        StringBuilder childBuild=new StringBuilder();
        childBuild.append(String.valueOf(tours.get(position).getChilds())).append( " childs");
        viewHolder.adult.setText(adultsBuild.toString());
        viewHolder.child.setText(childBuild.toString());

        // set min and max cost
        DecimalFormat dcf=new DecimalFormat("#,###");
        DecimalFormatSymbols dfs=new DecimalFormatSymbols(Locale.getDefault());
        dfs.setGroupingSeparator('.');
        dcf.setDecimalFormatSymbols(dfs);

        double min=Double.parseDouble(tours.get(position).getMinCost());
        double max=Double.parseDouble(tours.get(position).getMaxCost());

        StringBuilder costBuild=new StringBuilder();
        String minCost=dcf.format(min);
        String maxCost=dcf.format(max);
        costBuild.append(minCost).append("-").append(maxCost);
        viewHolder.cost.setText(costBuild.toString());

        return viewRow;
    }
}
