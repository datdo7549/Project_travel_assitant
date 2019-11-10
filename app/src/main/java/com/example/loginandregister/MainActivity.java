package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Adapter.CustomAdapter;
import com.example.loginandregister.Model.ListTour;
import com.example.loginandregister.Model.Tour;
import com.facebook.login.LoginManager;
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
    private Spinner byname;
    private Spinner isDesc;
    private Button get;
    private Button logout;
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
        logout.setOnClickListener(this);

    }

    private void mapping() {
        row=findViewById(R.id.rowPerPage);
        page=findViewById(R.id.pageNum);

        byname=findViewById(R.id.orderBy);
        ArrayAdapter<CharSequence> adapter_byname = ArrayAdapter.createFromResource(this, R.array.order_by, android.R.layout.simple_spinner_item);
        adapter_byname.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        byname.setAdapter(adapter_byname);

        isDesc=findViewById(R.id.isDesc);
        ArrayAdapter<CharSequence> adapter_isdesc = ArrayAdapter.createFromResource(this, R.array.is_desc, android.R.layout.simple_spinner_item);
        adapter_isdesc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        isDesc.setAdapter(adapter_isdesc);

        get=findViewById(R.id.getlist);
        logout=findViewById(R.id.logout);
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
                if (!checkEmpty(row, page)) {
                    Toast.makeText(this, "Row or Page is empty", Toast.LENGTH_SHORT).show();
                    break;
                }
                int mRow=Integer.parseInt(row.getText().toString());
                int mPage=Integer.parseInt(page.getText().toString());
                String mByname = byname.getSelectedItem().toString();
                boolean mIsDesc;
                if (isDesc.getSelectedItem().toString() == "Descending")
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
                            Toast.makeText(MainActivity.this,"Get khong thanh cong",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int total = response.body().getTotal();
                        if (checkValidRow(m_Row, m_Page, total)) {
                            arrayList = new ArrayList<>(total);
                            arrayList = response.body().getTourList();

                            Toast.makeText(MainActivity.this, "Get thanh cong ", Toast.LENGTH_SHORT).show();

                            arrayList = new ArrayList<>(total);
                            arrayList = response.body().getTourList();

                            ListView lv = findViewById(R.id.listTour);
                            CustomAdapter arrayAdapternew = new CustomAdapter(MainActivity.this, R.layout.custom_layout_tour_listview, arrayList);


                            //Sua cai nay ne, chuyen no thanh List View

                            arrayAdapternew.notifyDataSetChanged();
                            lv.setAdapter(arrayAdapternew);

                            Toast.makeText(MainActivity.this, "Get thanh cong ", Toast.LENGTH_SHORT).show();
                            ////////////////////////////
                        } else {
                            Toast.makeText(MainActivity.this, "Nhap RowPerPage va Page Num khong dung", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ListTour> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.logout:
            {
                LoginManager.getInstance().logOut();
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private boolean checkEmpty(EditText row, EditText page) {
        if (TextUtils.isEmpty(row.getText().toString()) || TextUtils.isEmpty(page.getText().toString()))
            return false;
        return true;
    }

    private boolean checkValidRow(int mRow, int mPage, int total) {
        double temp = total/(mPage-1);
        int max_row = (int) Math.round(temp);
        if (mRow <= max_row) {
            return true;
        }
        return false;
    }
}
