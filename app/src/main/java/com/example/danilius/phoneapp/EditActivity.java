package com.example.danilius.phoneapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.example.danilius.phoneapp.data.PhoneContract;

import java.io.IOException;
import java.io.StringReader;

public class EditActivity extends AppCompatActivity {
    public TextView namefield, phonefield, emailfield;
    private Button btnSave;
    private Button btnDelete;
    private Button btnCall;
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private String name,email;
    private Integer phonenumber;
    private PhoneBook phoneBook;
    private static final int REQUEST_CALL = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        btnSave = (Button) findViewById(R.id.button_save);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnCall = (Button) findViewById(R.id.button_call);
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
        phoneBook = new PhoneBook("", 0, "");
        int permissionStatus = ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.CALL_PHONE);
        if (arguments != null) {
            phoneBook = (PhoneBook) arguments.getSerializable(PhoneBook.class.getSimpleName());

            namefield.setText(phoneBook.getName());
            phonefield.setText(phoneBook.getPhone().toString());
            emailfield.setText(phoneBook.getEmail());

        }

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
                //db.execSQL("UPDATE phonebook SET name = "+name+", phonenumber= "+phonenumber+", email="+email+" WHERE name = "+phoneBook.getName()+" and phonenumber= "+phoneBook.getPhone()+" and email="+phoneBook.getEmail()+";");
                db.update("phonebook", cv, "name = ? AND phonenumber = ? AND email = ?", new String[]{phoneBook.getName(), String.valueOf(phoneBook.getPhone()), phoneBook.getEmail()});
                //db.update("phonebook",cv,PhoneContract.PhoneEntry.COLUMN_NAME+"="+phoneBook.getName()+"and"+PhoneContract.PhoneEntry.COLUMN_PHONENUMBER+"="+String.valueOf(phoneBook.getPhone())+"and"+PhoneContract.PhoneEntry.COLUMN_EMAIL+"="+phoneBook.getEmail(),null);
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        btnSave.setOnClickListener(oclBtnSave);

        View.OnClickListener oclBtnDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = Integer.getInteger(phonefield.getText().toString());
                db.execSQL("DELETE FROM phonebook WHERE phonenumber=" + phoneBook.getPhone().toString() + ";");
                // установка текста элемента TextView
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        btnDelete.setOnClickListener(oclBtnDelete);

        View.OnClickListener oclBtnCall = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uri address = Uri.parse(String.valueOf(phoneBook.getPhone()));
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneBook.getPhone()));
                startActivity(intent);
            }

        };
        btnCall.setOnClickListener(oclBtnCall);

    }
}