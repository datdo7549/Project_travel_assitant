package com.ygaps.travelapp.Model;

public class Notification {

    private String message;
    private String tour_id;

    public Notification(String message, String tour_id) {
        this.message = message;
        this.tour_id = tour_id;
    }

    public String getMessage() {
        return message;
    }

    public String getTour_id() {
        return tour_id;
    }
}
