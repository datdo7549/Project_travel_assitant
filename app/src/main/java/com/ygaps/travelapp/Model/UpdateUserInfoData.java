package com.ygaps.travelapp.Model;

import java.util.Date;

public class UpdateUserInfoData {
    private String fullName;
    private String email;
    private String phone;
    private int gender;
    private Date dob;


    public UpdateUserInfoData(String fullName, String email, String phone, int gender, Date dob) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.dob = dob;
    }
}
