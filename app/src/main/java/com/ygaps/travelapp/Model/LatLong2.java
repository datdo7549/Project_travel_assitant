package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class LatLong2 {
    private double lat;
    @SerializedName("long")
    private double mLong;

    public LatLong2(double lat, double mLong) {
        this.lat = lat;
        this.mLong = mLong;
    }
}
