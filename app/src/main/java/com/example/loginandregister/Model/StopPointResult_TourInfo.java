package com.example.loginandregister.Model;

import com.google.gson.annotations.SerializedName;

public class StopPointResult_TourInfo {
    private int id;
    private String name;
    private String lat;
    @SerializedName("long")
    private String mLong;
    private String arrivalAt;
    private String leaveAt;
    private String minCost;
    private String maxCost;
    private int serviceTypeId;
    private String avatar;

    private int check;


    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getArrivalAt() {
        return arrivalAt;
    }

    public String getLeaveAt() {
        return leaveAt;
    }

    public String getMinCost() {
        return minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getmLong() {
        return mLong;
    }
}
