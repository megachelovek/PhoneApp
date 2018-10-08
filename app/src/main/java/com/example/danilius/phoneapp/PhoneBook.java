package com.example.danilius.phoneapp;

import java.io.Serializable;

public class PhoneBook implements Serializable {
    private String mName;
    private String mPhone;
    private String mEmail;

    public PhoneBook(String name, String phone, String email) {
        mName = name;
        mPhone = phone;
        mEmail = email;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getEmail() {
        return mEmail;
    }
}
