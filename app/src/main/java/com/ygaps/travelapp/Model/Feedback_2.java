package com.ygaps.travelapp.Model;

import java.util.logging.StreamHandler;

public class Feedback_2 {
    private int id;
    private String userId;
    private String name;
    private String phone;
    private String avatar;
    private String feedback;
    private int point;
    private String createdOn;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getFeedback() {
        return feedback;
    }

    public int getPoint() {
        return point;
    }

    public String getCreatedOn() {
        return createdOn;
    }
}
