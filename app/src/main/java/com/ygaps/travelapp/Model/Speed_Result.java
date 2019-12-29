package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class Speed_Result {
    private Double lat;
    @SerializedName("long")
    private Double mLong;
    private int speed;
    private int createdByTourId;

    public Double getLat() {
        return lat;
    }

    public Double getmLong() {
        return mLong;
    }

    public int getSpeed() {
        return speed;
    }

    public int getCreatedByTourId() {
        return createdByTourId;
    }
}
