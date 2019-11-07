package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.loginandregister.Model.RegisterResult;
import com.example.loginandregister.Model.User_register;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.loginandregister.LoginActivity.URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText password;
    private EditText fullname;
    private EditText email;
    private EditText phone;
    private EditText address;
    private EditText dob;
    private EditText gender;
    private Button register;
    private Button back;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mapping();
        register.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void mapping() {
        password=findViewById(R.id.password);
        fullname=findViewById(R.id.fullName);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        dob=findViewById(R.id.dob);
        gender=findViewById(R.id.gender);
        register=findViewById(R.id.register_button);
        back=findViewById(R.id.back);
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
            case R.id.register_button:
            {
                String mPassword=password.getText().toString();
                String mFullname=fullname.getText().toString();
                String mEmail=email.getText().toString();
                String mPhone=phone.getText().toString();
                String mAddress=address.getText().toString();
                String mDob=dob.getText().toString();
                int mGender=Integer.parseInt(gender.getText().toString());
                progressDialog=new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Register Status");
                progressDialog.setMessage("Registering...");
                User_register user_register=new User_register(mPassword,mFullname,mEmail,mPhone,mAddress,mDob,mGender);
                Call<RegisterResult> call=jsonPlaceHolderApi.register(user_register);
                call.enqueue(new Callback<RegisterResult>() {
                    @Override
                    public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"Đăng kí không thành công",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        progressDialog.dismiss();
                        back.setVisibility(View.VISIBLE);
                        register.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this,"Đăng kí thành công",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<RegisterResult> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.back:
            {
                Bundle bundle=new Bundle();
                bundle.putString("email",email.getText().toString());
                bundle.putString("password",password.getText().toString());
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            }
        }
    }
}
