package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class SendCoordinate_Data {
    private String userId;
    private String tourId;
    private String lat;

    @SerializedName("long")
    private String mlong;

    public SendCoordinate_Data(String userId, String tourId, String lat, String mlong) {
        this.userId = userId;
        this.tourId = tourId;
        this.lat = lat;
        this.mlong = mlong;
    }
}
