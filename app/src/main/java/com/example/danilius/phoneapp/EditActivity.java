package com.example.danilius.phoneapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {
    public TextView namefield, phonefield, emailfield;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setPadding(16, 16, 16, 16);
        namefield = (TextView) findViewById(R.id.name);
        phonefield = (TextView) findViewById(R.id.phone_number);
        emailfield = (TextView) findViewById(R.id.email);
        Bundle arguments = getIntent().getExtras();
        final PhoneBook phoneBook;
        if (arguments != null) {
            phoneBook = (PhoneBook) arguments.getSerializable(PhoneBook.class.getSimpleName());
            namefield.setText(phoneBook.getName());
            phonefield.setText(phoneBook.getPhone());
            emailfield.setText(phoneBook.getEmail());

        }
    }
}