package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class LatLong {
    private int lat;
    @SerializedName("long")
    private int mLong;

    public int getLat() {
        return lat;
    }

    public int getmLong() {
        return mLong;
    }
}
