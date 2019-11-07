package com.example.loginandregister;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.loginandregister.Model.RegisterResult;
import com.example.loginandregister.Model.User_register;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

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
    private RadioGroup gender;
    private RadioButton genderMale;
    private RadioButton genderFemale;

    private Button register;
    private Button back;
    private ImageView back_button;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mapping();
        register.setOnClickListener(this);
        back.setOnClickListener(this);
        back_button.setOnClickListener(this);
    }

    private void mapping() {
        password = (EditText) findViewById(R.id.password);
        fullname = (EditText) findViewById(R.id.fullName);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);
        dob = (EditText) findViewById(R.id.dob);
        gender = (RadioGroup) findViewById(R.id.gender);
        genderMale = (RadioButton) findViewById(R.id.radioButton_male);
        genderFemale = (RadioButton) findViewById(R.id.radioButton_female);

        back_button = (ImageView) findViewById(R.id.bttBack);
        register = (Button) findViewById(R.id.register_button);
        back = (Button) findViewById(R.id.back);
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
                int mGender;
                if (checkValid(mPassword, mEmail, mDob, mFullname, mAddress, mPhone)) {
                    if (genderMale.isChecked()) {
                        mGender = 1;
                    } else {
                        mGender = 0;
                    }
                    String[] parts = mDob.split("/");
                    mDob = parts[2]+"-"+parts[1]+"-"+parts[0];
                    sendRegister(mPassword, mFullname, mEmail, mPhone, mAddress, mDob, mGender);
                }

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
            case R.id.bttBack:
            {
                onBackPressed();
                break;
            }
        }
    }

    private void sendRegister(String mPassword, String mFullname, String mEmail, String mPhone, String mAddress, String mDob, int mGender) {
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
                    Toast.makeText(RegisterActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.dismiss();
                back.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);
                Toast.makeText(RegisterActivity.this,"Complete",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkValid(String mPassword, String mEmail, String mDob, String mFullname, String mAddress, String mPhone) {
        String str_alert="";
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if(mEmail.matches(regex) == false) {
            if (str_alert!="")
                str_alert=str_alert+"\n";
            str_alert=str_alert+"Email is invalid";
        }
        if(mPassword.length() <= 6) {
            if (str_alert!="")
                str_alert=str_alert+"\n";
            str_alert=str_alert+"Password length must be above 6";
        }
        if(gender.getCheckedRadioButtonId() == -1) {
            if (str_alert!="")
                str_alert=str_alert+"\n";
            str_alert = str_alert + "Please check a gender";
        }
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            df.setLenient(false);
            df.parse(mDob);
        } catch (ParseException e) {
            if (str_alert!="")
                str_alert=str_alert+"\n";
            str_alert = str_alert + "Date format is not valid";
        }

        if (str_alert == "")
            return true;
        Toast.makeText(RegisterActivity.this, str_alert, Toast.LENGTH_LONG).show();
        return false;
    }
}
