package com.example.loginandregister.Model;

public class User_register {
    private String password;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String dob;
    private int gender;

    public User_register(String password, String fullName, String email, String phone, String address, String dob, int gender) {
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.gender = gender;
    }
}
