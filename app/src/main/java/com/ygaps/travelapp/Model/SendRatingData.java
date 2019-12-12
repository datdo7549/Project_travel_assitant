package com.ygaps.travelapp.Model;

public class SendRatingData {
    private int tourId;
    private int point;
    private String review;

    public SendRatingData(int tourId, int point, String review) {
        this.tourId = tourId;
        this.point = point;
        this.review = review;
    }
}
