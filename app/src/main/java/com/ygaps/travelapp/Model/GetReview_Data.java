package com.ygaps.travelapp.Model;

public class GetReview_Data {
    private int tourId;
    private int pageIndex;
    private String pageSize;

    public GetReview_Data(int tourId, int pageIndex, String pageSize) {
        this.tourId = tourId;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
}
