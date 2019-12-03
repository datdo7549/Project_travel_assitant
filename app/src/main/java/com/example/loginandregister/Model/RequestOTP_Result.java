package com.example.loginandregister.Model;

public class RequestOTP_Result {
    private int userId;
    private String type;
    private String expiredOn;

    public int getUserId() {
        return userId;
    }

    public String getType() {
        return type;
    }

    public String getExpiredOn() {
        return expiredOn;
    }
}
