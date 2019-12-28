package com.ygaps.travelapp;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.Adapter.CustomAdapter;
import com.ygaps.travelapp.Adapter.CustomAdapter_Feedback;
import com.ygaps.travelapp.Adapter.CustomAdapter_Suggest_Stop_Point;
import com.ygaps.travelapp.Model.CoordList;
import com.ygaps.travelapp.Model.CoordinateSet;
import com.ygaps.travelapp.Model.Feedback_2;
import com.ygaps.travelapp.Model.GetFeedBackList_Result;
import com.ygaps.travelapp.Model.GetSugestStopPoint_Result;
import com.ygaps.travelapp.Model.GetSuggestStoppoint_Data;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.SendFeedback_Data;
import com.ygaps.travelapp.Model.StopPoint2;
import com.ygaps.travelapp.Model.Tour;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.facebook.FacebookSdk.getApplicationContext;
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
    private Dialog suggest_stop_point_info;
    private CustomAdapter_Feedback customAdapter_feedback;
    private Dialog add_feedback;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_explore, menu);
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem itemSearch=menu.findItem(R.id.search_suggets_stop_point);

        SearchView searchView= (SearchView) itemSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                int len_strFind=s.length();

                s.toLowerCase();
                if(len_strFind==0) { // tra ve list view dang co
                    customAdapter_suggest_stop_point = new CustomAdapter_Suggest_Stop_Point(getContext(),R.layout.custom_item_suggest_stop_point, arrayList);
                    customAdapter_suggest_stop_point.notifyDataSetChanged();
                    listView.setAdapter(customAdapter_suggest_stop_point);
                }
                else
                {
                    ArrayList<StopPoint2> Temp=new ArrayList<>(); // tao 1 array adapter moi
                    for(int i=0;i<arrayList.size();i++)
                    {
                        StopPoint2 model=arrayList.get(i);
                        String name=model.getName();

                        String dataName="";

                        if(name==null )// name null
                        {
                            continue;
                        }
                        if((len_strFind > name.length()))
                            continue;


                        int index=0;

                        while(index<len_strFind) {
                            if(index<name.length())
                                dataName+=name.charAt(index);

                            index++;
//                            if(index==len_strFind)
//                                break;
                        }


                        if((dataName.compareToIgnoreCase(s)) == 0 ) {
                            Temp.add(model);
                        }

                    }

                    if(Temp.size()>0) {
                        customAdapter_suggest_stop_point = new CustomAdapter_Suggest_Stop_Point(getContext(), R.layout.custom_item_suggest_stop_point, Temp);
                        customAdapter_suggest_stop_point.notifyDataSetChanged();
                        listView.setAdapter(customAdapter_suggest_stop_point);
                    }
                    else
                    {
                        customAdapter_suggest_stop_point= new CustomAdapter_Suggest_Stop_Point(getContext(), R.layout.custom_item_suggest_stop_point, Temp);
                        customAdapter_suggest_stop_point.notifyDataSetInvalidated();
                        listView.setAdapter(customAdapter_suggest_stop_point);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_explore, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();

        setHasOptionsMenu(true);
        mapping();
        final Map<String, String> map = new HashMap<>();
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
                    arrayList=response.body().getStopPoints();
                    customAdapter_suggest_stop_point=new CustomAdapter_Suggest_Stop_Point(getContext(),R.layout.custom_item_suggest_stop_point,arrayList);
                    listView.setAdapter(customAdapter_suggest_stop_point);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            suggest_stop_point_info.setContentView(R.layout.stop_point_info_popup);
                            ImageView image_stop_point = suggest_stop_point_info.findViewById(R.id.image_stop_point_info);
                            TextView name_stop_point = suggest_stop_point_info.findViewById(R.id.name_stop_point_info);
                            TextView cost_stop_point = suggest_stop_point_info.findViewById(R.id.cost_stop_point_info);
                            TextView type_service = suggest_stop_point_info.findViewById(R.id.type_service_stop_point);
                            ImageView exit_stop_point_info_popup = suggest_stop_point_info.findViewById(R.id.exit_stop_point_info_popup);

                            ImageView add_feedback_image=suggest_stop_point_info.findViewById(R.id.add_feedback);
                            Call<GetFeedBackList_Result> call1_5=jsonPlaceHolderApi.getFeedback(map,arrayList.get(position).getId(),1,"100");
                            call1_5.enqueue(new Callback<GetFeedBackList_Result>() {
                                @Override
                                public void onResponse(Call<GetFeedBackList_Result> call, Response<GetFeedBackList_Result> response) {
                                    if (!response.isSuccessful())
                                    {
                                        Toast.makeText(getContext(),"Khong thanh cong",Toast.LENGTH_SHORT).show();
                                    }
                                    else {

                                        ArrayList<Feedback_2> arrayFeedback=response.body().getFeedbackList();
                                        ListView listView=suggest_stop_point_info.findViewById(R.id.list_feedback);
                                        customAdapter_feedback=new CustomAdapter_Feedback(getContext(),R.layout.custom_feedback_stop_point,arrayFeedback);
                                        listView.setAdapter(customAdapter_feedback);
                                    }
                                }

                                @Override
                                public void onFailure(Call<GetFeedBackList_Result> call, Throwable t) {
                                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });

                            add_feedback_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    add_feedback.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                                    add_feedback.setContentView(R.layout.addfeedback_popup);

                                    final TextView feedback_textview=add_feedback.findViewById(R.id.feedback_edit);
                                    Button add_feedback_btn=add_feedback.findViewById(R.id.send_feedback);
                                    final RatingBar ratingBar = add_feedback.findViewById(R.id.rating_point_feedback);



                                    add_feedback_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String feedback=feedback_textview.getText().toString();
                                            int point=(int)ratingBar.getRating();
                                            SendFeedback_Data sendFeedback_data=new SendFeedback_Data(arrayList.get(position).getId(),feedback,point);
                                            Call<InviteMember_Result> call1_6=jsonPlaceHolderApi.send_feed_back(map,sendFeedback_data);
                                            call1_6.enqueue(new Callback<InviteMember_Result>() {
                                                @Override
                                                public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                                                    if (!response.isSuccessful())
                                                    {
                                                        Toast.makeText(getApplicationContext(),"Khong thanh cong"+response.code(),Toast.LENGTH_SHORT).show();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(),"Thanh cong",Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                                @Override
                                                public void onFailure(Call<InviteMember_Result> call, Throwable t) {
                                                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });

                                    add_feedback.show();





                                }
                            });
                            exit_stop_point_info_popup.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    suggest_stop_point_info.dismiss();
                                }
                            });
                            Picasso.get()
                                    .load("https://cdn.pixabay.com/photo/2016/03/04/19/36/beach-1236581_960_720.jpg")
                                    .fit()
                                    .into(image_stop_point);
                            name_stop_point.setText(arrayList.get(position).getName());


                            //Xu ly cost
                            DecimalFormat dcf = new DecimalFormat("#,###");
                            DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
                            dfs.setGroupingSeparator('.');
                            dcf.setDecimalFormatSymbols(dfs);

                            int min =arrayList.get(position).getMinCost();
                            int max = arrayList.get(position).getMaxCost();

                            StringBuilder costBuild = new StringBuilder();
                            String minCost = dcf.format(min);
                            String maxCost = dcf.format(max);
                            costBuild.append(minCost).append("$ -> ").append(maxCost).append("$");
                            cost_stop_point.setText(costBuild);

                            type_service.setText(arrayList.get(position).getServiceTypeId() + "");



                            suggest_stop_point_info.show();
                        }
                    });
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

        suggest_stop_point_info = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        add_feedback = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(add_feedback.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
