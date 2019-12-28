package com.ygaps.travelapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Adapter.CustomAdapter_Notification;
import com.ygaps.travelapp.Model.Notification;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Notification_Fragment extends Fragment {
    private View view;
    private ListView listView;
    private String token;
    private ArrayList<Notification> arrayList=new ArrayList<>();
    private CustomAdapter_Notification customAdapter_notification;





    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        arrayList.clear();
        view=inflater.inflate(R.layout.fragment_notification, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();
        mapping();

        SharedPreferences sharedPreferences=this.getActivity().getSharedPreferences("com.ygaps.noti",MODE_PRIVATE);

        final int pos=sharedPreferences.getInt("POSITION",0);
        Toast.makeText(getContext(),pos+"",Toast.LENGTH_SHORT).show();


        for (int i=0;i<pos;i++)
        {
            String id_tour=sharedPreferences.getString("ID_TOUR"+i,"");
            String message=sharedPreferences.getString("MESSAGE"+i,"");
            Notification notification=new Notification(message,id_tour);
            arrayList.add(notification);
        }

        customAdapter_notification=new CustomAdapter_Notification(getContext(),R.layout.custom_item_notification,arrayList);
        listView.setAdapter(customAdapter_notification);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle=new Bundle();
                bundle.putString("id_tour",arrayList.get(position).getTour_id());
                bundle.putString("token_user",token);
                bundle.putString("MESSAGE_BODY",arrayList.get(position).getMessage());
                Intent intent=new Intent(getContext(),Temp.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        return view;
    }

    private void mapping() {
        listView=view.findViewById(R.id.list_notification);
    }


}
