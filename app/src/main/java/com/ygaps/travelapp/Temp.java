package com.ygaps.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Temp extends AppCompatActivity {
    private Button back_tif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.ygaps.travelapp.R.layout.activity_temp);
        back_tif=findViewById(com.ygaps.travelapp.R.id.back_tif);
        back_tif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle1=new Bundle();
                bundle1.putString("alo","CC ne");
                Intent intent=new Intent(Temp.this, Tour_Info.class);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

    }
}
