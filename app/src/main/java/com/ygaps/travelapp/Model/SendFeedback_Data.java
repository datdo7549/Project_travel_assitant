package com.ygaps.travelapp.Model;

public class SendFeedback_Data {
    private int serviceId;
    private String feedback;
    private int point;

    public SendFeedback_Data(int serviceId, String feedback, int point) {
        this.serviceId = serviceId;
        this.feedback = feedback;
        this.point = point;
    }
}
