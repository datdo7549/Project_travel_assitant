package com.ygaps.travelapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygaps.travelapp.Model.SendTokenFirebaseToServer_Result;
import com.ygaps.travelapp.Model.SendTokenFirebaseToSever_Data;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String myString="Helooooo";
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tabMenu;
    TabItem tabMyTour;
    TabItem tabExplore;
    TabItem tabNotification;
    TabItem tabSetting;
    ActionBar actionBar;

    private JsonPlaceHolderApi jsonPlaceHolderApi;
    public static final String URL="http://35.197.153.192:3000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);


        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        Bundle bundle=getIntent().getExtras();
        String token=bundle.getString("token");




        SharedPreferences sharedPreferences = getSharedPreferences("com.ygaps.travel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token_user",token);
        editor.apply();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d("DLTK", token);




        actionBar = getSupportActionBar();
        actionBar.setTitle("All tour");
        myString=token;
        tabLayout = findViewById(R.id.tablayout);
        tabMenu = findViewById(R.id.tabMenu);
        tabMyTour = findViewById(R.id.tabMyTour);
        tabExplore = findViewById(R.id.tabExplore);
        tabNotification=findViewById(R.id.tabNotification);
        tabSetting=findViewById(R.id.tabSetting);
        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    actionBar.setTitle("All tour");
                } else if (tab.getPosition() == 1) {
                    actionBar.setTitle("My tour");
                } else if (tab.getPosition() == 2)  {
                    actionBar.setTitle("Explore");
                } else if (tab.getPosition() == 3){
                    actionBar.setTitle("Notification");
                }else {
                    actionBar.setTitle("Setting");
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        sendTokenToServer(token, FirebaseInstanceId.getInstance().getToken());

    }


    public String getMyData() {
        return myString;
    }



    public void sendTokenToServer(String token_user,String token_firebase)
    {
        String device_id= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Map<String, String> map = new HashMap<>();
        map.put("Authorization",token_user);

        SendTokenFirebaseToSever_Data sendTokenFirebaseToSever_data=new SendTokenFirebaseToSever_Data(token_firebase,device_id,1,"1.0");

        Call<SendTokenFirebaseToServer_Result> call=jsonPlaceHolderApi.sendTokenToServer(map,sendTokenFirebaseToSever_data);
        call.enqueue(new Callback<SendTokenFirebaseToServer_Result>() {
            @Override
            public void onResponse(Call<SendTokenFirebaseToServer_Result> call, Response<SendTokenFirebaseToServer_Result> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"Gui token len server khong thanh cong",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Gui token len server thanh cong",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SendTokenFirebaseToServer_Result> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
