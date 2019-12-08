package com.ygaps.travelapp;

import com.ygaps.travelapp.Model.GGreesult;
import com.ygaps.travelapp.Model.GgData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonPlaceHolderAPI2 {
    @POST("token")
    Call<GGreesult> getAccess(@Body GgData ggData);
}
