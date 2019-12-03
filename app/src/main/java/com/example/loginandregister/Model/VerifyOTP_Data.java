package com.example.loginandregister.Model;

public class VerifyOTP_Data {
    private int userId;
    private String newPassword;
    private String verifyCode;

    public VerifyOTP_Data(int userId, String newPassword, String verifyCode) {
        this.userId = userId;
        this.newPassword = newPassword;
        this.verifyCode = verifyCode;
    }
}
