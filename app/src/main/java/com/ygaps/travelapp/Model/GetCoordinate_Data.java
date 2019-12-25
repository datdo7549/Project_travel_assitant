package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class GetCoordinate_Data {
    private String userId;
    private String tourId;
    private int lat;
    @SerializedName("long")
    private int mLong;


    public GetCoordinate_Data(String userId, String tourId, int lat, int mLong) {
        this.userId = userId;
        this.tourId = tourId;
        this.lat = lat;
        this.mLong = mLong;
    }
}
