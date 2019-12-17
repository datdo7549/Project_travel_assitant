package com.ygaps.travelapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ygaps.travelapp.Model.Tour;
import com.ygaps.travelapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class CustomAdapter extends ArrayAdapter<Tour>  {


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
        LinearLayout linearLayout;
        TextView name,date,adult,child,cost,detail;
        ImageButton btnCopy;
        LinearLayout edit_delete;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewRow=convertView;
        if(viewRow==null)
        {
            viewRow= layoutInflater.inflate(resource,parent,false);
            viewHolder.imageTour=viewRow.findViewById(R.id.image_tour);
            viewHolder.date=viewRow.findViewById(R.id.date_of_tour);
            viewHolder.detail=viewRow.findViewById(R.id.detail);
             /*
            viewHolder.imageName=viewRow.findViewById(R.id.imageName);
            viewHolder.imageDate=viewRow.findViewById(R.id.imageDate);
            viewHolder.imageAdult=viewRow.findViewById(R.id.imageAdults);
            viewHolder.imageChild=viewRow.findViewById(R.id.imageChilds);
            viewHolder.imageCost=viewRow.findViewById(R.id.imageCost);
            */
            viewHolder.name=viewRow.findViewById(R.id.name);
            viewHolder.edit_delete=viewRow.findViewById(R.id.edit_delete);
           // viewHolder.linearLayout=viewRow.findViewById(R.id.lnalo);
            /**
            viewHolder.date=viewRow.findViewById(R.id.date);
            viewHolder.adult=viewRow.findViewById(R.id.adults);
            viewHolder.child=viewRow.findViewById(R.id.childs);
            viewHolder.cost=viewRow.findViewById(R.id.min_max_Cost);
            viewHolder.btnCopy=viewRow.findViewById(R.id.btncopy);
             */
            viewRow.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) viewRow.getTag();
        }
        ArrayList<String> arrayListImage=new ArrayList<>();
        arrayListImage.add("https://images.unsplash.com/photo-1573913301579-8ef529408c15?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1506800075271-265b16abe1e7?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1351&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1557770402-145d15ef1596?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=700&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1570192659049-c4b377b85dae?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=675&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1488267580696-6fa71c9f7515?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80");
        arrayListImage.add("https://images.unsplash.com/photo-1515136011719-8766a8597ae1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=794&q=80");
        Random random;
        random=new Random();
        Picasso.get()
                .load(arrayListImage.get(random.nextInt(6)))
                .fit()
                .into(viewHolder.imageTour);


        // tim hinh anh phu hop
//        viewHolder.imageName.setImageResource(R.drawable.pass);
//        viewHolder.imageDate.setImageResource(R.drawable.pass);
//        viewHolder.imageAdult.setImageResource(R.drawable.pass);
//        viewHolder.imageChild.setImageResource(R.drawable.pass);
//        viewHolder.imageCost.setImageResource(R.drawable.pass);
        // xu li tiep phan gop chuoi

        viewHolder.name.setText(tours.get(position).getName());

        if(tours.get(position).getCheck()==0)
        {
            viewHolder.edit_delete.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.edit_delete.setVisibility(View.VISIBLE);
        }



        // xu li start vs end Date

        long miliStartDate=Long.parseLong(tours.get(position).getStartDate());
        final Date startD=new Date(miliStartDate);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");

        String temp1=dateFormat.format(startD);
        int day=Integer.parseInt(temp1.substring(0,2));
        int month=Integer.parseInt(temp1.substring(3,5));
        int year=Integer.parseInt(temp1.substring(6,10));

        String dateString = String.format("%d-%d-%d", year, month, day);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        String monthOfYear=new SimpleDateFormat("MMM", Locale.ENGLISH).format(date);
        viewHolder.date.setText(dayOfWeek+", "+day+" "+monthOfYear);

        viewHolder.detail.setText("Hà Nội tháng 5. Sen đã bắt đầu rời đầm về phố. Hồng liên,\nbạch liên, sen Hồ Tây, quỳ ở các đầm đua nhau bừng nở,\ntỏa hương thơm ngát...");
        /*
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

        */
        return viewRow;
    }



}
