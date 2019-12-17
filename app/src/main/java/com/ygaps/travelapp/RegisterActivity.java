package com.ygaps.travelapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Model.Message;
import com.ygaps.travelapp.Model.RegisterResult;
import com.ygaps.travelapp.Model.User_register;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;

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

    private TextView register;
    private Button back;
    private ImageView back_button;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_register);
        mapping();
        register.setOnClickListener(this);
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

        back_button = (ImageView) findViewById(R.id.back_homepage2);
        register =  findViewById(R.id.register_text_view);
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
            case R.id.register_text_view:
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
            case R.id.back_homepage2:
            {
                Intent intent=new Intent(this,Homepage.class);
                startActivity(intent);
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
                    if(response.code()==400) {
                        try {
                            JSONObject jObjError = null;
                            if (response.errorBody() != null) {
                                jObjError = new JSONObject(response.errorBody().string());
                            }
                            Gson gson = new Gson();
                            Type type = new TypeToken<ArrayList<Message>>(){}.getType();
                            ArrayList<Message> contactList = gson.fromJson(jObjError.get("message").toString(), type);
                            String err="";
                            for (Message mess : contactList){
                                err+=mess.getMsg()+"and";
                            }
                            String result=err.substring(0,err.length()-3);
                            Toast.makeText(RegisterActivity.this,"Error: "+result,Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(response.code()==503)
                    {
                        Toast.makeText(RegisterActivity.this,"Server error on creating user",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Complete", Toast.LENGTH_SHORT).show();
                    Bundle bundle=new Bundle();
                    bundle.putString("email",email.getText().toString());
                    bundle.putString("password",password.getText().toString());
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
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
