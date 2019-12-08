package com.ygaps.travelapp.Model;

public class Tour {
    private String id;
    private int status;
    private String name;
    private String minCost;
    private String maxCost;
    private String startDate;
    private String endDate;
    private int adults;
    private int childs;
    private boolean isPrivate;
    private String avatar;


    private int check;


    public Tour(String id, int status, String name, String minCost, String maxCost, String startDate, String endDate, int adults, int childs, boolean isPrivate, String avatar) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.startDate = startDate;
        this.endDate = endDate;
        this.adults = adults;
        this.childs = childs;
        this.isPrivate = isPrivate;
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getMinCost() {
        return minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public int getAdults() {
        return adults;
    }

    public int getChilds() {
        return childs;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
