package com.example.loginandregister.Model;

public class Create_Tour_Data {
    private int id;
    private String name;
    private long startDate;
    private long endDate;
    private Boolean isPrivate;
    private int adults;
    private int childs;
    private int minCost;
    private int maxCost;
    private int status;

    public Create_Tour_Data(String name, long startDate, long endDate, Boolean isPrivate, int adults, int childs, int minCost, int maxCost) {
        this.name = name;
        this.startDate = startDate;
        this.isPrivate = isPrivate;
        this.adults = adults;
        this.endDate = endDate;
        this.childs = childs;
        this.minCost = minCost;
        this.maxCost = maxCost;
    }
    public Create_Tour_Data(int id, String name, long startDate, long endDate, Boolean isPrivate, int adults, int childs, int minCost, int maxCost, int status) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPrivate = isPrivate;
        this.adults = adults;
        this.childs = childs;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.status = status;
    }
}
