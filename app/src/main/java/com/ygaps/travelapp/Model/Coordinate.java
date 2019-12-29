package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class Coordinate {
    private int id;
    private String lat;

    @SerializedName("long")
    private String mlong;

    public int getId() {
        return id;
    }

    public String getLat() {
        return lat;
    }

    public String getMlong() {
        return mlong;
    }
}
