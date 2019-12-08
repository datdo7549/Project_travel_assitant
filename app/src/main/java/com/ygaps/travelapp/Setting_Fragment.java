package com.ygaps.travelapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.facebook.login.LoginManager;


public class Setting_Fragment extends Fragment {

    private Button log_out;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(com.ygaps.travelapp.R.layout.fragment_setting, container, false);
        log_out=view.findViewById(com.ygaps.travelapp.R.id.btn_log_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent=new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
