package com.ygaps.travelapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygaps.travelapp.Adapter.CustomAdapter_Suggest_Stop_Point;
import com.ygaps.travelapp.Model.CoordList;
import com.ygaps.travelapp.Model.CoordinateSet;
import com.ygaps.travelapp.Model.GetSugestStopPoint_Result;
import com.ygaps.travelapp.Model.GetSuggestStoppoint_Data;
import com.ygaps.travelapp.Model.StopPoint2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Explore_Fragment extends Fragment {
    private View view;
    private ListView listView;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    private CustomAdapter_Suggest_Stop_Point customAdapter_suggest_stop_point;
    private ArrayList<StopPoint2> arrayList;
    private ArrayList<CoordList> coordLists=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_explore, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();


        mapping();
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", token);


        final GetSuggestStoppoint_Data getSuggestStoppoint_data=new GetSuggestStoppoint_Data(false,coordLists);
        Call<GetSugestStopPoint_Result> call1_44=jsonPlaceHolderApi.get_suggets_stop_point(map,getSuggestStoppoint_data);

        call1_44.enqueue(new Callback<GetSugestStopPoint_Result>() {
            @Override
            public void onResponse(Call<GetSugestStopPoint_Result> call, Response<GetSugestStopPoint_Result> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(getContext(),"Ko thanh cong"+response.code(),Toast.LENGTH_SHORT).show();
                }
                else {
                    customAdapter_suggest_stop_point=new CustomAdapter_Suggest_Stop_Point(getContext(),R.layout.custom_item_suggest_stop_point,response.body().getStopPoints());
                    listView.setAdapter(customAdapter_suggest_stop_point);
                }
            }

            @Override
            public void onFailure(Call<GetSugestStopPoint_Result> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void mapping() {
        listView=view.findViewById(R.id.suggest_stop_point);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        CoordinateSet coordinateSet_1=new CoordinateSet(48.033090,69.814943);
        CoordinateSet coordinateSet_2=new CoordinateSet(2.739697,43.903915);
        CoordinateSet coordinateSet_3=new CoordinateSet(2.739697,43.903915);
        CoordinateSet coordinateSet_4=new CoordinateSet(-6.658560,143.575005);
        CoordinateSet coordinateSet_5=new CoordinateSet(-6.658560,143.575005);
        CoordinateSet coordinateSet_6=new CoordinateSet(49.555488,143.322819);
        CoordinateSet coordinateSet_7=new CoordinateSet(49.555488,143.322819);
        CoordinateSet coordinateSet_8=new CoordinateSet(48.033090,69.814943);


        ArrayList<CoordinateSet> cs1=new ArrayList<>();
        cs1.add(coordinateSet_1);
        cs1.add(coordinateSet_2);

        ArrayList<CoordinateSet> cs2=new ArrayList<>();
        cs2.add(coordinateSet_3);
        cs2.add(coordinateSet_4);

        ArrayList<CoordinateSet> cs3=new ArrayList<>();
        cs3.add(coordinateSet_5);
        cs3.add(coordinateSet_6);

        ArrayList<CoordinateSet> cs4=new ArrayList<>();
        cs4.add(coordinateSet_7);
        cs4.add(coordinateSet_8);


        CoordList cl1=new CoordList(cs1);
        CoordList cl2=new CoordList(cs2);
        CoordList cl3=new CoordList(cs3);
        CoordList cl4=new CoordList(cs4);


        coordLists.add(cl1);
        coordLists.add(cl2);
        coordLists.add(cl3);
        coordLists.add(cl4);
    }

}
