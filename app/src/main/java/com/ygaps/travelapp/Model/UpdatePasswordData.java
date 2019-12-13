package com.ygaps.travelapp.Model;

public class UpdatePasswordData {
    private int userId;
    private String currentPassword;
    private String newPassword;

    public UpdatePasswordData(int userId, String currentPassword, String newPassword) {
        this.userId = userId;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
