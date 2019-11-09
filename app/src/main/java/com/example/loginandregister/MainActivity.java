package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText row;
    private EditText page;
    private EditText byname;
    private EditText isDesc;
    private Button get;
    private ListView listView;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String token;
    private ArrayList<Tour> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();
        Bundle bundle=getIntent().getExtras();
        token=bundle.getString("token");
        get.setOnClickListener(this);



    }

    private void mapping() {
        row=findViewById(R.id.rowPerPage);
        page=findViewById(R.id.pageNum);
        byname=findViewById(R.id.orderBy);
        isDesc=findViewById(R.id.isDesc);
        get=findViewById(R.id.getlist);
        //listView=findViewById(R.id.listview);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.getlist:
            {
                int mRow=Integer.parseInt(row.getText().toString());
                int mPage=Integer.parseInt(page.getText().toString());
                String mByname=byname.getText().toString();
                Boolean mIsDesc=Boolean.parseBoolean(isDesc.getText().toString());
                Map<String,String> map=new HashMap<>();
                map.put("Authorization",token);
                Call<ListTour> call=jsonPlaceHolderApi.getTour(mRow,mPage,mByname,mIsDesc,map);
                call.enqueue(new Callback<ListTour>() {
                    @Override
                    public void onResponse(Call<ListTour> call, Response<ListTour> response) {
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,"Get khong thanh cong",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        arrayList=new ArrayList<>(response.body().getTotal());
                        arrayList=response.body().getTourList();

                        Toast.makeText(MainActivity.this,"Get thanh cong ",Toast.LENGTH_SHORT).show();


                        arrayList=new ArrayList<>(response.body().getTotal());
                        arrayList=response.body().getTourList();

                        ListView lv=findViewById(R.id.listTour);
                        CustomAdapter arrayAdapternew= new CustomAdapter(MainActivity.this,R.layout.custom_layout_tour_listview,arrayList);


                        //Sua cai nay ne, chuyen no thanh List View

                        arrayAdapternew.notifyDataSetChanged();
                        lv.setAdapter(arrayAdapternew);

                        Toast.makeText(MainActivity.this,"Get thanh cong ",Toast.LENGTH_SHORT).show();
                        ////////////////////////////
                    }

                    @Override
                    public void onFailure(Call<ListTour> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
        }
    }
}
