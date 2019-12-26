package com.ygaps.travelapp.Model;

import com.google.gson.annotations.SerializedName;

public class StopPoint2 {
    private int id;
    private String name;
    private String address;
    private int provinceId;
    private String contact;
    private String lat;
    @SerializedName("long")
    private String mLong;
    private int minCost;
    private int maxCost;
    private int serviceTypeId;
    private String avatar;
    private String landingTimesOfUser;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getContact() {
        return contact;
    }

    public String getLat() {
        return lat;
    }

    public String getmLong() {
        return mLong;
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

    public String getAvatar() {
        return avatar;
    }

    public String getLandingTimesOfUser() {
        return landingTimesOfUser;
    }
}