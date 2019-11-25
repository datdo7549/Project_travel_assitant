package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Adapter.CustomAdapter;
import com.example.loginandregister.Model.Member;
import com.example.loginandregister.Model.StopPointResult_TourInfo;
import com.example.loginandregister.Model.TourInforData;
import com.example.loginandregister.Model.TourInforResult;
import com.example.loginandregister.Model.comment;
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

public class Tour_Info extends AppCompatActivity {

    private TextView tv;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String id;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour__info);
        tv=findViewById(R.id.tvv);

        Bundle bundle=getIntent().getExtras();
        id=bundle.getString("tour_id");
        token=bundle.getString("token");


        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);

        Map<String,String> map=new HashMap<>();
        map.put("Authorization",token);

        Call<TourInforResult> call=jsonPlaceHolderApi.getTourInfo(map,Integer.parseInt(id));
        call.enqueue(new Callback<TourInforResult>() {
            @Override
            public void onResponse(Call<TourInforResult> call, Response<TourInforResult> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(Tour_Info.this,"That bai",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Lay duoc danh sach Stop Point cua cai tour do:
                    ArrayList<StopPointResult_TourInfo> stopPointResult_tourInfo=response.body().getStopPoints();

                    //Lay duoc danh sach Comment cua cai tour do:
                    ArrayList<comment> arrayComment=response.body().getComments();

                    //Lay duoc danh sach Member cua cai tour do:
                    ArrayList<Member> arrayMember=response.body().getMembers();
                    Toast.makeText(Tour_Info.this,response.body().getId()+" "+response.body().getName(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourInforResult> call, Throwable t) {

            }
        });

    }
}
