package com.example.danilius.phoneapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.example.danilius.phoneapp.data.PhoneContract;

import java.io.IOException;

public class AddActivity extends AppCompatActivity {
    public TextView namefield, phonefield, emailfield;
    private Button btnSave;
    private Button btnDelete;
    private Button btnCall;
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private String name, email;
    private Integer phonenumber;
    private PhoneBook phoneBook;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btnSave = (Button) findViewById(R.id.button_save);
        dbHelper = new PhoneAppDbHelper(this);
        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setPadding(16, 16, 16, 16);
        namefield = (EditText) findViewById(R.id.name);
        phonefield = (EditText) findViewById(R.id.phone_number);
        emailfield = (EditText) findViewById(R.id.email);
        Bundle arguments = getIntent().getExtras();
        phoneBook = new PhoneBook("", Long.valueOf(0), "");
        int permissionStatus = ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CALL_PHONE);
        View.OnClickListener oclBtnSave = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = namefield.getText().toString();
                phonenumber = Integer.parseInt(phonefield.getText().toString());
                email = emailfield.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(PhoneContract.PhoneEntry.COLUMN_NAME, name);
                cv.put(PhoneContract.PhoneEntry.COLUMN_PHONENUMBER, phonenumber);
                cv.put(PhoneContract.PhoneEntry.COLUMN_EMAIL, email);
                db.insert("phonebook",null,cv);
                //db.execSQL("INSERT INTO phonebook VALUE(name = " + name + ", phonenumber= " + phonenumber + ", email=" + email+");");
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
              }
        };
        btnSave.setOnClickListener(oclBtnSave);

    }
}