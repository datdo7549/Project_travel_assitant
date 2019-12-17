package com.ygaps.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Homepage extends AppCompatActivity implements View.OnClickListener {
    private Button login_screen, sign_up_screen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_homepage2);
        mapping();
        init();
    }

    private void init() {
        login_screen.setOnClickListener(this);
        sign_up_screen.setOnClickListener(this);
    }

    private void mapping() {
        login_screen=findViewById(R.id.button_login_screen);
        sign_up_screen=findViewById(R.id.button_sign_up_screen);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId())
        {
            case R.id.button_login_screen:
            {
                intent=new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.button_sign_up_screen:
            {
                intent=new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
