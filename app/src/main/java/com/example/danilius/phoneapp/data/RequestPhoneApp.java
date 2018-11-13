package com.example.danilius.phoneapp.data;

import com.example.danilius.phoneapp.PhoneBook;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class RequestPhoneApp {
    public static void GETALL(DataOutputStream dos) throws IOException, InterruptedException {
        dos.writeUTF("GETALL");
        dos.flush();
    }
    public static void ADD(DataOutputStream dos, PhoneBook phoneBook) throws IOException, InterruptedException {
        dos.writeUTF("ADD "+phoneBook.getName()+" "+phoneBook.getPhone().toString()+" "+phoneBook.getEmail());
        dos.flush();
    }
    public static void DELETE(DataOutputStream dos, PhoneBook phoneBook) throws IOException, InterruptedException {
        dos.writeUTF("DELETE "+phoneBook.getName()+" "+phoneBook.getPhone().toString()+" "+phoneBook.getEmail());
        dos.flush();
    }
    public static void EDIT(DataOutputStream dos, PhoneBook phoneBook,PhoneBook newphoneBook) throws IOException, InterruptedException {
        dos.writeUTF("EDIT "+phoneBook.getName()+" "+phoneBook.getPhone().toString()+" "+phoneBook.getEmail()+" "+newphoneBook.getName()+" "+newphoneBook.getPhone().toString()+" "+newphoneBook.getEmail());
        dos.flush();
    }
}
