package com.example.loginandregister.Model;

import com.google.gson.annotations.SerializedName;

public class Stop_Point {
    private int id;
    private String name;
    private String address;
    private int provinceId;
    private double lat;

    @SerializedName("long")
    private double mlong;

    private long arrivalAt;
    private long leaveAt;
    private int serviceTypeId;
    private int minCost;
    private int maxCost;



    public Stop_Point(String name, String address, int provinceId, double lat, double mlong, long arrivalAt, long leaveAt, int serviceTypeId, int minCost, int maxCost) {
        this.name = name;
        this.address = address;
        this.provinceId = provinceId;
        this.lat = lat;
        this.mlong = mlong;
        this.arrivalAt = arrivalAt;
        this.leaveAt = leaveAt;
        this.serviceTypeId = serviceTypeId;
        this.minCost = minCost;
        this.maxCost = maxCost;
    }

    public Stop_Point(int id, String name, String address, int provinceId, double lat, double mlong, long arrivalAt, long leaveAt, int serviceTypeId, int minCost, int maxCost) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.provinceId = provinceId;
        this.lat = lat;
        this.mlong = mlong;
        this.arrivalAt = arrivalAt;
        this.leaveAt = leaveAt;
        this.serviceTypeId = serviceTypeId;
        this.minCost = minCost;
        this.maxCost = maxCost;
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

    public double getLat() {
        return lat;
    }

    public double getMlong() {
        return mlong;
    }

    public long getArrivalAt() {
        return arrivalAt;
    }

    public long getLeaveAt() {
        return leaveAt;
    }

    public int getServiceTypeId() {
        return serviceTypeId;
    }

    public int getMinCost() {
        return minCost;
    }

    public int getMaxCost() {
        return maxCost;
    }
}
