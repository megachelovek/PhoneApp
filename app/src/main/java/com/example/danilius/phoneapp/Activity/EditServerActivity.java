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

import com.example.danilius.phoneapp.Client;
import com.example.danilius.phoneapp.PhoneBook;
import com.example.danilius.phoneapp.R;
import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.example.danilius.phoneapp.data.PhoneContract;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class EditServerActivity extends AppCompatActivity implements IEditServerActivityCallback{
    public TextView namefield, phonefield, emailfield;
    private Button btnSave;
    private Button btnDelete;
    private Button btnCall;
    private ImageButton btnAvatar;
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private String name,email,ip,imagepath;
    private Long phonenumber;
    private int port;
    private PhoneBook phoneBook,newphonebook;
    Client client;
    private static final int REQUEST_CALL = 1;
    final int ACTIVITY_CHOOSE_FILE = 1;
    Bitmap bitmap = null;
    List<Bitmap> bitmapArray = new ArrayList<Bitmap>();
    Intent intentAvatar = new Intent();
    final int takeFlags = intentAvatar.getFlags()
            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editserver);
        btnSave = (Button) findViewById(R.id.button_save);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnCall = (Button) findViewById(R.id.button_call);
        btnAvatar = (ImageButton)  findViewById(R.id.imageButtonEdit);
        dbHelper = new PhoneAppDbHelper(this);
        newphonebook = new PhoneBook();

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
        phoneBook = new PhoneBook("", Long.getLong("0"), "");
        int permissionStatus = ContextCompat.checkSelfPermission(EditServerActivity.this, Manifest.permission.CALL_PHONE);
        if (arguments != null) {
            phoneBook = (PhoneBook) arguments.getSerializable(PhoneBook.class.getSimpleName());
            namefield.setText(phoneBook.getName());
            phonefield.setText(phoneBook.getPhone().toString());
            emailfield.setText(phoneBook.getEmail());
            if ((phoneBook.getImagePath()!=null)&&(phoneBook.getImagePath()!="")){
                imagepath = phoneBook.getImagePath();
                btnAvatar.setImageURI(Uri.parse(imagepath));
            }
        }

        View.OnClickListener oclBtnAvatar = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent chooseFile;

                if(ContextCompat.checkSelfPermission(EditServerActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                }
                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("image/*");
                intentAvatar = Intent.createChooser(chooseFile, "Выберите изображение");
                startActivityForResult(intentAvatar, ACTIVITY_CHOOSE_FILE);
            }
        };
        btnAvatar.setOnClickListener(oclBtnAvatar);
        //Сохранить
        View.OnClickListener oclBtnSave = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newphonebook.setName(namefield.getText().toString());
                newphonebook.setPhone(Long.parseLong(phonefield.getText().toString()));
                newphonebook.setEmail(emailfield.getText().toString());
                newphonebook.setImagePath(imagepath);
                client = new Client(ip,port, EditServerActivity.this , "EDIT",phoneBook,newphonebook);
                client.execute();
            }
        };
        btnSave.setOnClickListener(oclBtnSave);
        //Удалить
        View.OnClickListener oclBtnDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new Client(ip,port, EditServerActivity.this , "DELETE",phoneBook);
                client.execute();
            }
        };
        btnDelete.setOnClickListener(oclBtnDelete);
        //Позвонить
        View.OnClickListener oclBtnCall = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneBook.getPhone()));
                startActivity(intent);
            }

        };
        btnCall.setOnClickListener(oclBtnCall);

    }


    @Override
    public void callingBackEditServerActivity() {
        Intent intent = new Intent(EditServerActivity.this, ClientActivity.class);
        startActivity(intent);
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