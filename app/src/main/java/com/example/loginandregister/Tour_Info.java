package com.example.loginandregister;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Adapter.CustomAdapterForTourInfo_Comment;
import com.example.loginandregister.Adapter.CustomAdapterForTourInfo_Member;
import com.example.loginandregister.Adapter.CustomAdapterForTourInfo_StopPoint;
import com.example.loginandregister.Model.CommentResult_TourInfo;
import com.example.loginandregister.Model.Member;
import com.example.loginandregister.Model.StopPointResult_TourInfo;
import com.example.loginandregister.Model.TourInforResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
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
    private ImageView upload_image_tour_info;
    private TextView name_tour_info;
    private TextView date_tour_info;
    private TextView cost_tour_info;
    private TextView adult_tour_info;
    private TextView baby_tour_info;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String id;
    private String token;
    private ImageView imagee_tour_info;
    private ListView listView_stop_point;
    private ListView listView_comment;
    private ListView listView_member;
    private CustomAdapterForTourInfo_StopPoint customAdapterForTourInfoStopPoint;
    private CustomAdapterForTourInfo_Comment customAdapterForTourInfo_comment;
    private CustomAdapterForTourInfo_Member customAdapterForTourInfo_member;
    private ArrayList<String> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_tour__info);
        mapping();

        upload_image_tour_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }
        });
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
                    ArrayList<CommentResult_TourInfo> arrayComment=response.body().getComments();

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


                    //Xu li Stop Point
                    if (stopPointResult_tourInfo.size()!=0) {
                        customAdapterForTourInfoStopPoint = new CustomAdapterForTourInfo_StopPoint(Tour_Info.this, R.layout.list_stop_point_tour_info, stopPointResult_tourInfo);
                        listView_stop_point.setAdapter(customAdapterForTourInfoStopPoint);
                    }
                    else
                    {
                        listView_stop_point.setVisibility(View.GONE);
                        TextView stop_point_placeholder=findViewById(R.id.stop_point_placeholder);
                        stop_point_placeholder.setVisibility(View.VISIBLE);
                    }

                    //Xu ly commnt
                    if (arrayComment.size()!=0) {
                        customAdapterForTourInfo_comment = new CustomAdapterForTourInfo_Comment(Tour_Info.this, R.layout.list_comment_tour_info, arrayComment);
                        listView_comment.setAdapter(customAdapterForTourInfo_comment);
                    }
                    else
                    {
                        listView_comment.setVisibility(View.GONE);
                        TextView comment_placeholder=findViewById(R.id.comment_placeholder);
                        comment_placeholder.setVisibility(View.VISIBLE);
                    }

                    //Xu ly member
                    if (arrayMember.size()!=0) {
                        customAdapterForTourInfo_member = new CustomAdapterForTourInfo_Member(Tour_Info.this, R.layout.list_member_tour_info, arrayMember);
                        listView_member.setClipToOutline(true);
                        listView_member.setAdapter(customAdapterForTourInfo_member);
                    }
                    else
                    {
                        listView_member.setVisibility(View.GONE);
                        TextView membeer_placeholder=findViewById(R.id.member_placeholder);
                        membeer_placeholder.setVisibility(View.VISIBLE);
                    }

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
        upload_image_tour_info=findViewById(R.id.upload_image_tour_info);
        listView_stop_point=findViewById(R.id.list_stop_point_tour_info1);
        listView_comment=findViewById(R.id.list_comment_tour_info);
        listView_member=findViewById(R.id.list_member_tour_info);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==2 && resultCode==RESULT_OK)
        {
            Uri uri=data.getData();
            assert uri != null;
            File file=new File(uri.getPath());
            Toast.makeText(Tour_Info.this,file+"",Toast.LENGTH_SHORT).show();
        }
    }
}
