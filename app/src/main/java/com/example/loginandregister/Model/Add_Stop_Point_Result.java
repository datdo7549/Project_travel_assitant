package com.example.loginandregister.Model;

import com.google.gson.annotations.SerializedName;

public class Add_Stop_Point_Result {
    private int tourId;
    private String name;
    private double lat;

    @SerializedName("long")
    private double mLong;

    private long arrivalAt;
    private long leaveAt;
    private int minCost;
    private int maxCost;
    private int serviceTypeId;
    private int id;
    private String avatar;


    public int getTourId() {
        return tourId;
    }

    public String getName() {
        return name;
    }

    public double getLat() {
        return lat;
    }

    public long getArrivalAt() {
        return arrivalAt;
    }

    public long getLeaveAt() {
        return leaveAt;
    }

    public int getMinCost() {
        return minCost;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public int getId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }
}
