package com.ygaps.travelapp.Model;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Forgot_Frag_1;
import com.ygaps.travelapp.JsonPlaceHolderApi;
import com.ygaps.travelapp.LoginActivity;
import com.ygaps.travelapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygaps.travelapp.RegisterActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;

public class Forgot_Frag_2 extends Fragment {
    final static String DATA_RECEIVE = "data_receive";
    private EditText n1,n2,n3,n4,n5,n6;
    private EditText newPassword;
    private TextView send_new_pass;
    private ImageView back;
    private View viewRoot;
    private String userID;
    private String emailorphone;
    private OnClickButtonListener_2 onClickButtonListener_2;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    public void setOnClickButtonListener(OnClickButtonListener_2 onClickButtonListener_2) {
        this.onClickButtonListener_2 = onClickButtonListener_2;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot=inflater.inflate(R.layout.forgot_frag_2,container,false);
        mapping();
        send_new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OTP_receive=n1.getText().toString()+n2.getText().toString()+n3.getText().toString()+n4.getText().toString()+n5.getText().toString()+n6.getText().toString();
                VerifyOTP_Data verifyOTP_data=new VerifyOTP_Data(Integer.parseInt(userID),newPassword.getText().toString(),OTP_receive);
                Call<VerifyOTP_Result> call=jsonPlaceHolderApi.verifyOTP(verifyOTP_data);
                call.enqueue(new Callback<VerifyOTP_Result>() {
                    @Override
                    public void onResponse(Call<VerifyOTP_Result> call, Response<VerifyOTP_Result> response) {
                        if (!response.isSuccessful()) {

                            try {
                                Toast.makeText(getContext(), "Khong thanh cong" + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            Toast.makeText(getContext(),"Thanh cong",Toast.LENGTH_SHORT).show();
                            Bundle bundle=new Bundle();
                            bundle.putString("email",emailorphone);
                            bundle.putString("password",newPassword.getText().toString());
                            Intent intent=new Intent(getContext(),LoginActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyOTP_Result> call, Throwable t) {

                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonListener_2.backHome();
            }
        });
        return viewRoot;
    }

    private void mapping() {
        n1=viewRoot.findViewById(R.id.num1);
        n2=viewRoot.findViewById(R.id.num2);
        n3=viewRoot.findViewById(R.id.num3);
        n4=viewRoot.findViewById(R.id.num4);
        n5=viewRoot.findViewById(R.id.num5);
        n6=viewRoot.findViewById(R.id.num6);
        newPassword=viewRoot.findViewById(R.id.new_password);
        send_new_pass=viewRoot.findViewById(R.id.send_request);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        back=viewRoot.findViewById(R.id.back_homepage4);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            userID=args.getString(DATA_RECEIVE);
            emailorphone=args.getString("email_or_phone");
        }
    }

    public interface OnClickButtonListener_2
    {
        void backHome();
    }
}
