package com.ygaps.travelapp;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.ygaps.travelapp.Model.RequestOTP_Data;
import com.ygaps.travelapp.Model.RequestOTP_Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;

public class Forgot_Frag_1 extends Fragment {
    private View rootView;
    private EditText email_request;
    private Button send_requets_OTP;
    private OnClickButtonListener onClickButtonListener;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private ProgressDialog progressDialog;
    public void setOnClickButtonListener(OnClickButtonListener onClickButtonListener) {
        this.onClickButtonListener = onClickButtonListener;
    }
    DataPassListener mCallback;

    public interface DataPassListener{
        public void passData(String data);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try
        {
            mCallback =(DataPassListener) context;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()+ " must implement OnImageClickListener");
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.forgot_frag_1,container,false);
        mapping();
        send_requets_OTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Processing");
                progressDialog.setMessage("Sending...");
                progressDialog.show();
                final RequestOTP_Data requestOTP_data=new RequestOTP_Data("email",email_request.getText().toString());
                Call<RequestOTP_Result> call=jsonPlaceHolderApi.requestOTP(requestOTP_data);
                call.enqueue(new Callback<RequestOTP_Result>() {
                    @Override
                    public void onResponse(Call<RequestOTP_Result> call, Response<RequestOTP_Result> response) {
                        if (!response.isSuccessful())
                        {
                            Toast.makeText(getContext(),"Sending failed",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            int userID=response.body().getUserId();
                            mCallback.passData(userID+"");
                            onClickButtonListener.clickButton();

                        }
                    }

                    @Override
                    public void onFailure(Call<RequestOTP_Result> call, Throwable t) {
                    }
                });
            }
        });
        return rootView;
    }

    private void mapping() {
        send_requets_OTP=rootView.findViewById(R.id.send_request_OTP);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        email_request=rootView.findViewById(R.id.request_data);
        progressDialog=new ProgressDialog(getContext());
    }

    public interface OnClickButtonListener
    {
        void clickButton();
    }
}
