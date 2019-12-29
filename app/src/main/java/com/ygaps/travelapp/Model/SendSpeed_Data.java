package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class SendSpeed_Data {
    private Double lat;

    @SerializedName("long")
    private Double mLong;
    private int tourId;
    private int userId;
    private int notificationType;
    private int speed;

    public SendSpeed_Data(Double lat, Double mLong, int tourId, int userId, int notificationType, int speed) {
        this.lat = lat;
        this.mLong = mLong;
        this.tourId = tourId;
        this.userId = userId;
        this.notificationType = notificationType;
        this.speed = speed;
    }
}
