package com.ygaps.travelapp;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygaps.travelapp.Adapter.CustomAdapter;
import com.ygaps.travelapp.Model.ListTour;
import com.ygaps.travelapp.Model.Tour;

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
public class List_Tour_Fragment extends Fragment  {

    private EditText row;
    private EditText page;
    private Spinner byname;
    private Spinner isDesc;
    private Button getListTour;
    private Button logout;
    private ImageView createTour;
    private ListView lv;
    private CustomAdapter arrayAdapternew;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    private ArrayList<Tour> arrayList;
    private View view;
    private Dialog get_tour_pop_up;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final MainActivity activity = (MainActivity) getActivity();
        view=inflater.inflate(com.ygaps.travelapp.R.layout.fragment_list_tour, container, false);
        token = activity.getMyData();
        mapping();

        alo1239();
        getListTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button getListTour;
                get_tour_pop_up.setContentView(com.ygaps.travelapp.R.layout.get_tour_pop_up);
                getListTour=get_tour_pop_up.findViewById(com.ygaps.travelapp.R.id.button_get_tour);
                row=get_tour_pop_up.findViewById(com.ygaps.travelapp.R.id.rowPerPage);
                page=get_tour_pop_up.findViewById(com.ygaps.travelapp.R.id.pageNum);
                byname=get_tour_pop_up.findViewById(com.ygaps.travelapp.R.id.orderBy);
                ArrayAdapter<CharSequence> adapter_byname = ArrayAdapter.createFromResource(getContext(), com.ygaps.travelapp.R.array.order_by, android.R.layout.simple_spinner_item);
                adapter_byname.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                byname.setAdapter(adapter_byname);

                isDesc=get_tour_pop_up.findViewById(com.ygaps.travelapp.R.id.isDesc);
                ArrayAdapter<CharSequence> adapter_isdesc = ArrayAdapter.createFromResource(getContext(), com.ygaps.travelapp.R.array.is_desc, android.R.layout.simple_spinner_item);
                adapter_isdesc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                isDesc.setAdapter(adapter_isdesc);


                getListTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkEmpty(row, page)) {
                            Toast.makeText(getContext(), "Row or Page is empty", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            int mRow=Integer.parseInt(row.getText().toString());
                            int mPage=Integer.parseInt(page.getText().toString());
                            String mByname = byname.getSelectedItem().toString();
                            boolean mIsDesc;
                            if (isDesc.getSelectedItem().toString().equals("Descending"))
                                mIsDesc = true;
                            else
                                mIsDesc = false;
                            Map<String,String> map=new HashMap<>();
                            map.put("Authorization",token);
                            final int m_Row = mRow, m_Page = mPage;
                            Call<ListTour> call=jsonPlaceHolderApi.getTour(mRow,mPage,mByname,mIsDesc,map);
                            call.enqueue(new Callback<ListTour>() {
                                @Override
                                public void onResponse(Call<ListTour> call, Response<ListTour> response) {
                                    if(!response.isSuccessful())
                                    {
                                        Toast.makeText(getContext(),"Get khong thanh cong",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    else
                                    {
                                        int total = response.body().getTotal();
                                        if (checkValidRow(m_Row, m_Page, total)) {
                                            arrayList = new ArrayList<>(total);
                                            arrayList = response.body().getTourList();
                                            arrayList = new ArrayList<>(total);
                                            arrayList = response.body().getTourList();
                                            arrayAdapternew = new CustomAdapter(getContext(), com.ygaps.travelapp.R.layout.custom_layout_tour_listview, arrayList);
                                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Bundle bundle=new Bundle();
                                                    bundle.putString("tour_id",arrayList.get(position).getId());
                                                    bundle.putString("token",token);
                                                    Intent intent=new Intent(getContext(), Tour_Info.class);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                }
                                            });
                                            arrayAdapternew.notifyDataSetChanged();
                                            lv.setAdapter(arrayAdapternew);
                                            get_tour_pop_up.dismiss();
                                        }
                                        else {
                                            Toast.makeText(getContext(), "Nhap RowPerPage va Page Num khong dung", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ListTour> call, Throwable t) {
                                    Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

                get_tour_pop_up.show();
            }
        });
        //get.setOnClickListener(this);
        createTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("token",token);
                Intent intent=new Intent(getContext(), CreateActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return view;

    }

    private void alo1239() {
        Map<String,String> map=new HashMap<>();
        map.put("Authorization",token);
        //final int m_Row = mRow, m_Page = mPage;
        Call<ListTour> call=jsonPlaceHolderApi.getTour(400,1,"name",true,map);
        call.enqueue(new Callback<ListTour>() {
            @Override
            public void onResponse(Call<ListTour> call, Response<ListTour> response) {
                if(!response.isSuccessful())
                {
                    Toast.makeText(getContext(),"Get khong thanh cong",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    int total = response.body().getTotal();
                        arrayList = new ArrayList<>(total);
                        arrayList = response.body().getTourList();
                        arrayList = new ArrayList<>(total);
                        arrayList = response.body().getTourList();

                        lv = view.findViewById(com.ygaps.travelapp.R.id.listTour);
                        arrayAdapternew = new CustomAdapter(getContext(), com.ygaps.travelapp.R.layout.custom_layout_tour_listview, arrayList);

                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Bundle bundle=new Bundle();
                                bundle.putString("tour_id",arrayList.get(position).getId());
                                bundle.putString("token",token);
                                Intent intent=new Intent(getContext(), Tour_Info.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        arrayAdapternew.notifyDataSetChanged();
                        lv.setAdapter(arrayAdapternew);
                }
            }

            @Override
            public void onFailure(Call<ListTour> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void mapping() {
        get_tour_pop_up=new Dialog(getContext(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        get_tour_pop_up.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getListTour=view.findViewById(com.ygaps.travelapp.R.id.getListTour);
        createTour=view.findViewById(com.ygaps.travelapp.R.id.create_button);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
    }



    private boolean checkEmpty(EditText row, EditText page) {
        if (TextUtils.isEmpty(row.getText().toString()) || TextUtils.isEmpty(page.getText().toString()))
            return false;
        return true;
    }
    private boolean checkValidRow(int mRow, int mPage, int total) {
        double temp;
        int sub=mPage-1;
        if(sub==0) {
            temp = total;
        }
        else {
            temp = total / (mPage - 1);
        }
        int max_row = (int) Math.round(temp);
        if (mRow <= max_row) {
            return true;
        }
        return false;
    }



}
