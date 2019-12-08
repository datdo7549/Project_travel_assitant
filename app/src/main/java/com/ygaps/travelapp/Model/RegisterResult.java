package com.ygaps.travelapp.Model;

public class RegisterResult {
    private int id;
    private String username;
    private String full_name;
    private String email;
    private String phone;
    private String address;
    private String dob;
    private int gender;
    private boolean email_verified;
    private boolean phone_verified;


    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getDob() {
        return dob;
    }

    public int getGender() {
        return gender;
    }

    public boolean isEmail_verified() {
        return email_verified;
    }

    public boolean isPhone_verified() {
        return phone_verified;
    }
}
