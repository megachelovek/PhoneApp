package com.example.danilius.phoneapp;


import java.io.Serializable;

public class PhoneBook implements Serializable {
    private Integer Id;
    private String Name;
    private Long Phone;
    private String Email;

    public PhoneBook(String name, Long phonenumber, String email) {
        Name = name;
        Phone = phonenumber;
        Email = email;
    }

    public PhoneBook() {    }

    public Integer getId() {
        return Id;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    public void setPhone(Long phone) {
        Phone = phone;
    }

    public Long getPhone() {
        return Phone;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmail() {
        return Email;
    }


}
