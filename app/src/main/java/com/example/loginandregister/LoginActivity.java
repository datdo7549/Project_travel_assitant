package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginandregister.Model.LoginResult;
import com.example.loginandregister.Model.User_login;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String URL="http://35.197.153.192:3000/";
    private EditText emailPhone;
    private EditText password;
    private Button login;
    private TextView register;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mapping();
        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    private void mapping() {
        emailPhone=findViewById(R.id.edit_emailPhone);
        password=findViewById(R.id.edit_password);
        login=findViewById(R.id.login_button);
        register=findViewById(R.id.register);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_button:
            {
                String mEmailPhone=emailPhone.getText().toString();
                String mPassword=password.getText().toString();
                User_login user_login=new User_login(mEmailPhone,mPassword);
                Call<LoginResult> call=jsonPlaceHolderApi.login(user_login);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this,"Dang nhap khong thanh cong",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(LoginActivity.this,"Dang nhap thanh cong",Toast.LENGTH_SHORT).show();
                        Bundle bundle=new Bundle();
                        bundle.putString("token",response.body().getToken());
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.register:
            {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            emailPhone.setText(bundle.getString("email", ""));
            password.setText(bundle.getString("password", ""));
        }
    }
}
