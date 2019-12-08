package com.ygaps.travelapp.Model;

public class Create_Tour_Result {
    private String hostId;
    private int status;
    private String name;
    private int minCost;
    private int maxCost;
    private long startDate;
    private long endDate;
    private int adults;
    private int childs;
    private int sourceLat;
    private int sourceLong;
    private int desLat;
    private int desLong;
    private int id;
    private Boolean isPrivate;
    private String avatar;

    public String getHostId() {
        return hostId;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public int getMinCost() {
        return minCost;
    }

    public int getMaxCost() {
        return maxCost;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public int getAdults() {
        return adults;
    }

    public int getChilds() {
        return childs;
    }

    public int getSourceLat() {
        return sourceLat;
    }

    public int getSourceLong() {
        return sourceLong;
    }

    public int getDesLat() {
        return desLat;
    }

    public int getDesLong() {
        return desLong;
    }

    public int getId() {
        return id;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public String getAvatar() {
        return avatar;
    }
}
