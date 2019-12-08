package com.ygaps.travelapp.Model;

public class User_login {
    private String emailPhone;
    private String password;

    public User_login(String emailPhone, String password) {
        this.emailPhone = emailPhone;
        this.password = password;
    }

    public String getEmailPhone() {
        return emailPhone;
    }

    public String getPassword() {
        return password;
    }
}
