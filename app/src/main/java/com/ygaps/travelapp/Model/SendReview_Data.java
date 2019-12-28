package com.ygaps.travelapp.Model;

public class SendReview_Data {
    private int tourId;
    private int point;
    private String review;

    public SendReview_Data(int tourId, int point, String review) {
        this.tourId = tourId;
        this.point = point;
        this.review = review;
    }
}
