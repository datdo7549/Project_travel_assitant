package com.ygaps.travelapp;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Adapter.CustomAdapter;
import com.ygaps.travelapp.Model.Create_Tour_Data;
import com.ygaps.travelapp.Model.Create_Tour_Result;
import com.ygaps.travelapp.Model.InviteData;
import com.ygaps.travelapp.Model.InviteMemberData;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.InviteResult;
import com.ygaps.travelapp.Model.My_Tour_Result;
import com.ygaps.travelapp.Model.SendTokenFirebaseToServer_Result;
import com.ygaps.travelapp.Model.SendTokenFirebaseToSever_Data;
import com.ygaps.travelapp.Model.Tour;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class my_tour_fragment extends Fragment {
    private TextView guide;
    private View view;
    private ListView listView;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    private ArrayList<Tour> arrayList=new ArrayList<>();
    private ArrayList<Tour> tours_current=new ArrayList<>();
    private CustomAdapter arrayAdapternew;
    private Button getMyListTour;
    private Dialog option_get_tour_popup;
    private Button getMyTourPopUp;
    private String user_id;
    public static final String URL="http://35.197.153.192:3000/";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_my_tour, menu);
        MenuItem itemSearch=menu.findItem(R.id.search_my_tour);
        final MainActivity activity = (MainActivity) getActivity();
        user_id=activity.getUser_id();
        Toast.makeText(getContext(),user_id+"",Toast.LENGTH_SHORT).show();
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
                    arrayAdapternew = new CustomAdapter(getContext(),R.layout.custom_layout_tour_listview, tours_current);
                    arrayAdapternew.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapternew);
                }
                else
                {
                    List<Tour> Temp=new ArrayList<>(); // tao 1 array adapter moi
                    for(int i=0;i<tours_current.size();i++)
                    {
                        Tour model=tours_current.get(i);
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
                        arrayAdapternew = new CustomAdapter(getContext(), R.layout.custom_layout_tour_listview, Temp);
                        arrayAdapternew.notifyDataSetChanged();
                        listView.setAdapter(arrayAdapternew);
                    }
                    else
                    {
                        arrayAdapternew= new CustomAdapter(getContext(), R.layout.custom_layout_tour_listview, Temp);
                        arrayAdapternew.notifyDataSetInvalidated();
                        listView.setAdapter(arrayAdapternew);
                    }
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.get_my_tour) {
            GetMyTour();
        }
        return true;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();
        setHasOptionsMenu(true);
        view=inflater.inflate(R.layout.fragment_my_tour, container, false);

        mapping();


        /*getMyListTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guide.setVisibility(View.GONE);
                Button normal;
                option_get_tour_popup.setContentView(R.layout.get_my_tour_pop_up);
                option_get_tour_popup.show();
                normal=option_get_tour_popup.findViewById(R.id.getMyTour_Normal);
                getMyTourPopUp=option_get_tour_popup.findViewById(R.id.get_my_tour_in_popup);
                normal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout ln_normal=option_get_tour_popup.findViewById(R.id.ln_normal);
                        ln_normal.setVisibility(View.VISIBLE);
                        getMyTourPopUp.setVisibility(View.VISIBLE);
                        getMyTourPopUp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText pageIndex=option_get_tour_popup.findViewById(R.id.pageIndex);
                                EditText pageSize=option_get_tour_popup.findViewById(R.id.pageSize);
                                if (checkEmpty(pageIndex,pageSize))
                                {
                                    int mPageIndex=Integer.parseInt(pageIndex.getText().toString());
                                    int mPageSize=Integer.parseInt(pageSize.getText().toString());
                                    listView.setVisibility(View.VISIBLE);
                                    option_get_tour_popup.dismiss();
                                    loadMyTour(mPageIndex,mPageSize);
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Input full info please",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    }
                });


            }
        });*/
        return view;
    }
    private boolean checkEmpty(EditText row, EditText page) {
        if (TextUtils.isEmpty(row.getText().toString()) || TextUtils.isEmpty(page.getText().toString()))
            return false;
        return true;
    }
    private void loadMyTour(final int a, int b) {
        final Map<String,String> map=new HashMap<>();
        map.put("Authorization",token);
        Call<My_Tour_Result> call=jsonPlaceHolderApi.getMyTour(map,a,b);
        call.enqueue(new Callback<My_Tour_Result>() {
            @Override
            public void onResponse(final Call<My_Tour_Result> call, Response<My_Tour_Result> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getContext(),"Get failed",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    int total = response.body().getTotal();
                    arrayList = new ArrayList<>(total);
                    arrayList = response.body().getTours();
                    final Animation animation= AnimationUtils.loadAnimation(getContext(),R.anim.animation );
                    tours_current.clear();
                    for (int i=0;i<arrayList.size();i++)
                    {
                        if (arrayList.get(i).getStatus()!=-1)
                        {
                            tours_current.add(arrayList.get(i));
                        }
                    }
                    Toast.makeText(getContext(),tours_current.size()+"",Toast.LENGTH_SHORT).show();
                    arrayAdapternew = new CustomAdapter(getContext(), R.layout.custom_layout_tour_listview, tours_current);
                    arrayAdapternew.notifyDataSetChanged();
                    listView.setAdapter(arrayAdapternew);
                    listView.setClipToOutline(true);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if(tours_current.get(position).getCheck()==1)
                            {
                                LinearLayout edit_delete=view.findViewById(R.id.edit_delete);
                                arrayList.get(position).setCheck(0);
                                edit_delete.setVisibility(View.INVISIBLE);
                            }
                            else {
                                Bundle bundle = new Bundle();
                                bundle.putString("tour_id", tours_current.get(position).getId());
                                bundle.putString("user_id",user_id);
                                bundle.putString("token", token);
                                Intent intent = new Intent(getContext(), Tour_Info.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    });
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                            final int d=position;
                            LinearLayout edit_delete=view.findViewById(R.id.edit_delete);
                            tours_current.get(position).setCheck(1);
                            edit_delete.startAnimation(animation);
                            edit_delete.setVisibility(View.VISIBLE);
                            Button edit=view.findViewById(R.id.edit_tour);
                            Button delete=view.findViewById(R.id.delete_tour);
                            edit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String name_temp=arrayList.get(position).getName();
                                    Bundle bundle=new Bundle();
                                    Intent intent=new Intent(getContext(),CreateActivity.class);
                                    bundle.putString("token",token);
                                    bundle.putInt("id_tour",Integer.parseInt(tours_current.get(position).getId()));
                                    bundle.putString("name_temp",name_temp);
                                    bundle.putInt("mode",1);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Create_Tour_Data upadte_tour_data = new Create_Tour_Data(Integer.parseInt(tours_current.get(position).getId()), "", 12, 23, true, 1, 1, 1, 2, -1);
                                    Map<String, String> map = new HashMap<>();
                                    map.put("Authorization", token);
                                    Call<Create_Tour_Result> call = jsonPlaceHolderApi.updateTour(map, upadte_tour_data);
                                    call.enqueue(new Callback<Create_Tour_Result>() {
                                        @Override
                                        public void onResponse(Call<Create_Tour_Result> call, Response<Create_Tour_Result> response) {
                                            if (!response.isSuccessful()) {
                                                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                            } else {

                                                Toast.makeText(getContext(), "Successfully", Toast.LENGTH_SHORT).show();
                                                tours_current.remove(position);
                                                arrayAdapternew.notifyDataSetChanged();
                                                listView.setAdapter(arrayAdapternew);

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Create_Tour_Result> call, Throwable t) {
                                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            return true;
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<My_Tour_Result> call, Throwable t) {

            }
        });
    }

    private void mapping() {
        listView=view.findViewById(R.id.myListTour);
        option_get_tour_popup=new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        option_get_tour_popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        guide=view.findViewById(R.id.guide);
        //getMyListTour=view.findViewById(R.id.getMyListTour);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
    }

    @Override
    public void onResume() {
        Log.d("DAT","onResume");
        if (arrayList.size()!=0) {
            for (int i=0;i<arrayList.size();i++)
            {
                arrayList.get(i).setCheck(0);
            }
            arrayAdapternew.notifyDataSetChanged();
        }
        super.onResume();
    }

    public void GetMyTour()
    {
        guide.setVisibility(View.GONE);
        Button normal;
        option_get_tour_popup.setContentView(R.layout.get_my_tour_pop_up);
        option_get_tour_popup.show();
        getMyTourPopUp=option_get_tour_popup.findViewById(R.id.get_my_tour_in_popup);
        getMyTourPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pageIndex=option_get_tour_popup.findViewById(R.id.pageIndex);
                EditText pageSize=option_get_tour_popup.findViewById(R.id.pageSize);
                if (checkEmpty(pageIndex,pageSize))
                {
                    int mPageIndex=Integer.parseInt(pageIndex.getText().toString());
                    int mPageSize=Integer.parseInt(pageSize.getText().toString());
                    listView.setVisibility(View.VISIBLE);
                    option_get_tour_popup.dismiss();
                    loadMyTour(mPageIndex,mPageSize);
                }
                else
                {
                    Toast.makeText(getContext(),"Vui lòng điền đủ thông tin",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }




}
