package com.example.danilius.phoneapp.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.danilius.phoneapp.PhoneBook;
import com.example.danilius.phoneapp.R;
import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.example.danilius.phoneapp.data.PhoneContract;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class AddActivity extends AppCompatActivity {
    public TextView namefield, phonefield, emailfield;
    private Button btnSave;
    private Button btnDelete;
    private Button btnCall;
    private ImageButton btnAvatar;
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private String name, email,imagepath;
    private Long phonenumber;
    private PhoneBook phoneBook;
    Intent intentAvatar = new Intent();
    final int ACTIVITY_CHOOSE_FILE = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        btnSave = (Button) findViewById(R.id.button_save);
        btnAvatar = (ImageButton)  findViewById(R.id.imageButtonAdd);
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
        //ПЕРЕДАЧА НА СЕРВЕР
        View.OnClickListener oclBtnSave = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = namefield.getText().toString();
                phonenumber = Long.parseLong(phonefield.getText().toString());
                email = emailfield.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(PhoneContract.PhoneEntry.COLUMN_NAME, name);
                cv.put(PhoneContract.PhoneEntry.COLUMN_PHONENUMBER, phonenumber);
                cv.put(PhoneContract.PhoneEntry.COLUMN_EMAIL, email);
                cv.put(PhoneContract.PhoneEntry.COLUMN_IMAGEPATH, imagepath);
                db.insert("phonebook",null,cv);
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
              }
        };
        btnSave.setOnClickListener(oclBtnSave);
        //ВЫБОР КАРТИНКИ
        View.OnClickListener oclBtnAvatar = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent chooseFile;
                if(ContextCompat.checkSelfPermission(AddActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                intentAvatar = Intent.createChooser(chooseFile, "Выберите изображение");
                startActivityForResult(intentAvatar, ACTIVITY_CHOOSE_FILE);
            }
        };
        btnAvatar.setOnClickListener(oclBtnAvatar);
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_CHOOSE_FILE: {
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String fileName=new File(uri.getPath()).getName();
                    File CacheFile = new File(getApplication().getCacheDir()+"/" + fileName);
                    try {
                        CacheFile.delete();
                        Bitmap bitmapNew = getBitmapFromUri(uri);
                        FileChannel outChannel = new FileOutputStream(CacheFile).getChannel();
                        bitmapNew.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(CacheFile));
                        imagepath = getApplication().getCacheDir() +"/" + fileName;
                        if (outChannel != null)
                            outChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    btnAvatar.setImageURI(Uri.parse(imagepath));

                }
            }

        }
    }
}