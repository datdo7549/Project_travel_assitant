package com.ygaps.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ygaps.travelapp.Model.Forgot_Frag_2;


public class ForgotActivity extends AppCompatActivity implements Forgot_Frag_1.DataPassListener {
    public static final String DATA_RECEIVE = "data_receive";
    private Forgot_Frag_1 forgot_frag_1;
    private Forgot_Frag_2 forgot_frag_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        initFrag();
        show_forgot_frag_1();
    }

    public void show_forgot_frag_1() {
        getSupportFragmentManager().beginTransaction().show(forgot_frag_1).hide(forgot_frag_2).commit();
    }
    public void show_forgot_frag_2() {
        getSupportFragmentManager().beginTransaction().show(forgot_frag_2).hide(forgot_frag_1).commit();
    }


    private void initFrag() {
        forgot_frag_1=new Forgot_Frag_1();
        forgot_frag_1.setOnClickButtonListener(new Forgot_Frag_1.OnClickButtonListener() {
            @Override
            public void clickButton() {
                show_forgot_frag_2();
            }
        });
        forgot_frag_2=new Forgot_Frag_2();
        forgot_frag_2.setOnClickButtonListener(new Forgot_Frag_1.OnClickButtonListener() {
            @Override
            public void clickButton() {

            }
        });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_main,forgot_frag_1,Forgot_Frag_1.class.getName())
                .add(R.id.id_main,forgot_frag_2,Forgot_Frag_2.class.getName())
                .commit();
    }


    @Override
    public void passData(String data) {
        Forgot_Frag_2 fragmentB = new Forgot_Frag_2 ();
        Bundle args = new Bundle();
        args.putString("data_receive", data);
        fragmentB .setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_main, fragmentB )
                .commit();
    }
}
