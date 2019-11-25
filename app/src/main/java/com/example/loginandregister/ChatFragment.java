package com.example.loginandregister;


import android.content.Context;
import android.content.Intent;
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
import androidx.viewpager.widget.ViewPager;

import com.example.loginandregister.Adapter.CustomAdapter;
import com.example.loginandregister.Model.ListTour;
import com.example.loginandregister.Model.Tour;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.loginandregister.LoginActivity.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment implements View.OnClickListener {

    private EditText row;
    private EditText page;
    private Spinner byname;
    private Spinner isDesc;
    private Button get;
    private Button logout;
    private Button createTour;
    private ListView lv;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    private ArrayList<Tour> arrayList;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final MainActivity activity = (MainActivity) getActivity();
        view=inflater.inflate(R.layout.fragment_chat, container, false);
        token = activity.getMyData();
        mapping();

        get.setOnClickListener(this);
        createTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("token",token);
                Intent intent=new Intent(getContext(),CreateActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;

    }



    private void mapping() {
        row=view.findViewById(R.id.rowPerPage);
        page=view.findViewById(R.id.pageNum);

        byname=view.findViewById(R.id.orderBy);
        ArrayAdapter<CharSequence> adapter_byname = ArrayAdapter.createFromResource(getContext(), R.array.order_by, android.R.layout.simple_spinner_item);
        adapter_byname.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        byname.setAdapter(adapter_byname);

        isDesc=view.findViewById(R.id.isDesc);
        ArrayAdapter<CharSequence> adapter_isdesc = ArrayAdapter.createFromResource(getContext(), R.array.is_desc, android.R.layout.simple_spinner_item);
        adapter_isdesc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isDesc.setAdapter(adapter_isdesc);

        get=view.findViewById(R.id.getlist);
        logout=view.findViewById(R.id.logout);
        createTour=view.findViewById(R.id.create_tour);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.getlist:
            {
                if (!checkEmpty(row, page)) {
                    Toast.makeText(getContext(), "Row or Page is empty", Toast.LENGTH_SHORT).show();
                    break;
                }
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
                        else {
                            int total = response.body().getTotal();
                            if (checkValidRow(m_Row, m_Page, total)) {
                                arrayList = new ArrayList<>(total);
                                arrayList = response.body().getTourList();

                                Toast.makeText(getContext(), "Get thanh cong ", Toast.LENGTH_SHORT).show();

                                arrayList = new ArrayList<>(total);
                                arrayList = response.body().getTourList();

                                lv = view.findViewById(R.id.listTour);
                                CustomAdapter arrayAdapternew = new CustomAdapter(getContext(), R.layout.custom_layout_tour_listview, arrayList);

                                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                        Bundle bundle=new Bundle();
                                        bundle.putString("tour_id",arrayList.get(position).getId());
                                        bundle.putString("token",token);
                                        Intent intent=new Intent(getContext(),Tour_Info.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });

                                //Sua cai nay ne, chuyen no thanh List View

                                arrayAdapternew.notifyDataSetChanged();
                                lv.setAdapter(arrayAdapternew);


                                Toast.makeText(getContext(), "Get thanh cong ", Toast.LENGTH_SHORT).show();
                                ////////////////////////////
                            } else {
                                Toast.makeText(getContext(), "Nhap RowPerPage va Page Num khong dung", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTour> call, Throwable t) {
                        Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
        }
    }


}
