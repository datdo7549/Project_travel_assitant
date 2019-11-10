package com.example.loginandregister;

import com.example.loginandregister.Model.GGreesult;
import com.example.loginandregister.Model.GgData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI2 {
    @POST("token")
    Call<GGreesult> getAccess(@Body GgData ggData);
}
