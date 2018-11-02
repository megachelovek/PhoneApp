package com.example.danilius.phoneapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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


import com.example.danilius.phoneapp.PhoneBook;
import com.example.danilius.phoneapp.R;
import com.example.danilius.phoneapp.data.PhoneAppDbHelper;

public class CallActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_call);
        btnCall = (Button) findViewById(R.id.button_call);
        phonefield = (EditText) findViewById(R.id.phone_number);

        View.OnClickListener oclBtnCall = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phonefield.getText()));
                if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(CallActivity.this,new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else {
                    startActivity(intent);
            }
            }
        };
        btnCall.setOnClickListener(oclBtnCall);

    }
}