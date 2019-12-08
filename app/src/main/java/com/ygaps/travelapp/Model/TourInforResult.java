package com.ygaps.travelapp.Model;

import java.util.ArrayList;

public class TourInforResult {
    private int id;
    private String hostId;
    private int status;
    private String name;
    private String minCost;
    private String maxCost;
    private String startDate;
    private String endDate;
    private int adults;
    private int childs;
    private Boolean isPrivate;
    private ArrayList<StopPointResult_TourInfo> stopPoints;
    private ArrayList<CommentResult_TourInfo> comments;
    private ArrayList<Member> members;


    public int getId() {
        return id;
    }

    public String getHostId() {
        return hostId;
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

    public Boolean getPrivate() {
        return isPrivate;
    }

    public ArrayList<StopPointResult_TourInfo> getStopPoints() {
        return stopPoints;
    }

    public ArrayList<CommentResult_TourInfo> getComments() {
        return comments;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }
}
