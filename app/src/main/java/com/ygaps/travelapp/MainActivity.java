package com.ygaps.travelapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        Bundle bundle=getIntent().getExtras();
        String token=bundle.getString("token");

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


    }


    public String getMyData() {
        return myString;
    }
}
