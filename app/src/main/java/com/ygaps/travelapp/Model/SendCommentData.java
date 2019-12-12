package com.ygaps.travelapp.Model;

public class SendCommentData {
    private String tourId;
    private String userId;
    private String comment;

    public SendCommentData(String tourId, String userId, String comment) {
        this.tourId = tourId;
        this.userId = userId;
        this.comment = comment;
    }
}
