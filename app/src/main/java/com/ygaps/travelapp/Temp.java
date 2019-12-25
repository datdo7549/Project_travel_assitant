package com.ygaps.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.UserResponde_Data;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;

public class Temp extends AppCompatActivity {

    public static final String ACCEPT_ACTION = "Accept";
    public static final String DECLINE_ACTION = "Decline";
    public static final String SHOW_ACTION = "Show";
    public static final String URL="http://35.197.153.192:3000/";
    private Intent intent;
    private TextView tvMessage;
    private TextView tvResponseStatus;
    private Button btnAccept;
    private Button btnDecline;
    private LinearLayout layoutButton;
    private JsonPlaceHolderApi jsonPlaceHolderApi;


    private String token_user;
    private String id_tour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);


        mapping();
        hideNotification();
        addAction();
    }

    private void addAction() {
        String action=intent.getAction();
        if(action==null){
            return;
        }

        switch (action)
        {
            case ACCEPT_ACTION:
                acceptAction();
                break;
            case DECLINE_ACTION:
                declineAction();
                break;
            case SHOW_ACTION:
                showAction();
                break;
            default:
                finish();
                break;
        }
    }

    private void showAction() {
        layoutButton.setVisibility(LinearLayout.VISIBLE);
        tvResponseStatus.setText("Waiting response...");
        tvResponseStatus.setTextColor(Color.MAGENTA);
    }

    private void hideNotification() {
        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra("NOTIFICATION_ID",-1));
    }

    private void mapping() {

        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        intent=getIntent();
        tvMessage=findViewById(R.id.tvMessageContent);
        tvResponseStatus=findViewById(R.id.tvResponseStatus);
        btnAccept=findViewById(R.id.btnAccept);
        btnDecline=findViewById(R.id.btnDecline);
        layoutButton=findViewById(R.id.layoutButton);
        layoutButton.setVisibility(LinearLayout.INVISIBLE);

        Bundle layThongBao=new Bundle();
        layThongBao=intent.getExtras();
        token_user=layThongBao.getString("token_user");
        id_tour=layThongBao.getString("id_tour");
        tvMessage.setText(layThongBao.getString("MESSAGE_BODY"));



        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutButton.setVisibility(LinearLayout.INVISIBLE);
                acceptAction();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutButton.setVisibility(LinearLayout.INVISIBLE);
                declineAction();
            }
        });
    }



    private void declineAction() {
        tvResponseStatus.setText("Declined!");
        tvResponseStatus.setTextColor(Color.RED);
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", token_user);
        UserResponde_Data userResponde_data=new UserResponde_Data(id_tour,false);
        Call<InviteMember_Result> call=jsonPlaceHolderApi.responde_user(map,userResponde_data);
        call.enqueue(new Callback<InviteMember_Result>() {
            @Override
            public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Decline Knong thanh cong",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Decline thanh cong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InviteMember_Result> call, Throwable t) {

            }
        });
        Toast.makeText(this,"You have been declined this invitation",Toast.LENGTH_SHORT);
        // xu li tu choi o day
    }

    private void acceptAction() {
        tvResponseStatus.setText("Accepted!");
        tvResponseStatus.setTextColor(Color.GREEN);
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", token_user);
        UserResponde_Data userResponde_data=new UserResponde_Data(id_tour,true);
        Call<InviteMember_Result> call=jsonPlaceHolderApi.responde_user(map,userResponde_data);
        call.enqueue(new Callback<InviteMember_Result>() {
            @Override
            public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Accept Knong thanh cong",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(getApplicationContext(),"Accept thanh cong",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InviteMember_Result> call, Throwable t) {

            }
        });
        Toast.makeText(this,"You have been accepted this invitation",Toast.LENGTH_SHORT);
        // xu li dong y o day
    }
}
