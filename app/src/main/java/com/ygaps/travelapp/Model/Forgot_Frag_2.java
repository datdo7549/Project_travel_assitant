package com.ygaps.travelapp.Model;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;

public class Forgot_Frag_2 extends Fragment {
    final static String DATA_RECEIVE = "data_receive";
    private EditText OTP_receive;
    private EditText newPassword;
    private Button send_new_pass;
    private Button back_to;
    private View viewRoot;
    private String userID;
    private Forgot_Frag_1.OnClickButtonListener onClickButtonListener;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    public void setOnClickButtonListener(Forgot_Frag_1.OnClickButtonListener onClickButtonListener) {
        this.onClickButtonListener = onClickButtonListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewRoot=inflater.inflate(R.layout.forgot_frag_2,container,false);
        mapping();
        send_new_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerifyOTP_Data verifyOTP_data=new VerifyOTP_Data(Integer.parseInt(userID),newPassword.getText().toString(),OTP_receive.getText().toString());
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
                            send_new_pass.setVisibility(View.GONE);
                            back_to.setVisibility(View.VISIBLE);
                            back_to.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent=new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<VerifyOTP_Result> call, Throwable t) {

                    }
                });
            }
        });
        return viewRoot;
    }

    private void mapping() {
        OTP_receive=viewRoot.findViewById(R.id.OTP);
        newPassword=viewRoot.findViewById(R.id.new_password);
        send_new_pass=viewRoot.findViewById(R.id.send_request);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        back_to=viewRoot.findViewById(R.id.back_to_login_2);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            userID=args.getString(DATA_RECEIVE);
        }
    }
}
