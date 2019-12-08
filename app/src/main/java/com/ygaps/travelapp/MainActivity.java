package com.ygaps.travelapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {

    private String myString="Helooooo";
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem tabMenu;
    TabItem tabMyTour;
    TabItem tabExplore;
    TabItem tabNotification;
    TabItem tabSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String URL="http://35.197.153.192:3000/";
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(com.ygaps.travelapp.R.layout.activity_main);

        Bundle bundle=getIntent().getExtras();
        String token=bundle.getString("token");
        myString=token;
        tabLayout = findViewById(com.ygaps.travelapp.R.id.tablayout);
        tabMenu = findViewById(com.ygaps.travelapp.R.id.tabMenu);
        tabMyTour = findViewById(com.ygaps.travelapp.R.id.tabMyTour);
        tabExplore = findViewById(com.ygaps.travelapp.R.id.tabExplore);
        tabNotification=findViewById(com.ygaps.travelapp.R.id.tabNotification);
        tabSetting=findViewById(com.ygaps.travelapp.R.id.tabSetting);
        viewPager = findViewById(com.ygaps.travelapp.R.id.viewPager);
        
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        sendFcmToken();

    }

    private void sendFcmToken() { // gui token den server
        // String token1= FirebaseInstanceId.getInstance().getToken();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token=sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");

        // xu li gui token den server


    }


    public String getMyData() {
        return myString;
    }
}
