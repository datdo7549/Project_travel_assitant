package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.loginandregister.LoginActivity.URL;

public class Tour_Info extends AppCompatActivity {

    private TextView name_tour_info;
    private TextView date_tour_info;
    private TextView cost_tour_info;
    private TextView adult_tour_info;
    private TextView baby_tour_info;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String id;
    private String token;
    private ImageView imagee_tour_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_tour__info);

        mapping();
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size. x;
        imagee_tour_info=findViewById(R.id.image_tour_info);
        Picasso.get()
                .load("https://cdn.pixabay.com/photo/2016/03/04/19/36/beach-1236581_960_720.jpg")
                .fit()
                .into(imagee_tour_info);
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
                    name_tour_info.setText(response.body().getName());
                    long miliStartDate=Long.parseLong(response.body().getStartDate());
                    final Date startD=new Date(miliStartDate);
                    DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
                    String temp1=dateFormat.format(startD);
                    StringBuilder dateBuild=new StringBuilder();

                    //Xu ly date
                    long miliEndDate=Long.parseLong(response.body().getEndDate());
                    Date endD=new Date(miliEndDate);
                    DateFormat dateFormat1=new SimpleDateFormat("dd/MM/yyyy");
                    String temp2=dateFormat1.format(endD);
                    dateBuild.append(temp1).append(" -> ").append(temp2);
                    date_tour_info.setText(dateBuild);

                    //Xu ly cost
                    DecimalFormat dcf=new DecimalFormat("#,###");
                    DecimalFormatSymbols dfs=new DecimalFormatSymbols(Locale.getDefault());
                    dfs.setGroupingSeparator('.');
                    dcf.setDecimalFormatSymbols(dfs);

                    double min=Double.parseDouble(response.body().getMinCost());
                    double max=Double.parseDouble(response.body().getMaxCost());

                    StringBuilder costBuild=new StringBuilder();
                    String minCost=dcf.format(min);
                    String maxCost=dcf.format(max);
                    costBuild.append(minCost).append("$ -> ").append(maxCost).append("$");
                    cost_tour_info.setText(costBuild);
                    //Xu ly adult, child
                    adult_tour_info.setText(response.body().getAdults()+"");
                    baby_tour_info.setText(response.body().getChilds()+"");

                    Toast.makeText(Tour_Info.this,response.body().getStopPoints().size()+"" ,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<TourInforResult> call, Throwable t) {

            }
        });

    }

    private void mapping() {
        name_tour_info=findViewById(R.id.name_tour_info);
        date_tour_info=findViewById(R.id.date_tour_info);
        cost_tour_info=findViewById(R.id.cost_tour_info);
        adult_tour_info=findViewById(R.id.adult_tour_info);
        baby_tour_info=findViewById(R.id.baby_tour_info);
    }

}
