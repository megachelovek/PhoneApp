package com.example.danilius.phoneapp.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.danilius.phoneapp.PhoneBook;
import com.example.danilius.phoneapp.R;
import com.example.danilius.phoneapp.SaveLoadImage;
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
import java.util.Random;

import static android.os.Environment.getExternalStorageDirectory;
import static com.example.danilius.phoneapp.data.PhoneAppDbHelper.LOG_TAG;
import static java.io.File.separator;

public class EditActivity extends AppCompatActivity {
    public TextView namefield, phonefield, emailfield;
    private Button btnSave;
    private Button btnDelete;
    private Button btnCall;
    private ImageButton btnAvatar;
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private String name,email, imagepath;
    private Long phonenumber;
    private PhoneBook phoneBook;
    private static final int REQUEST_CALL = 1;
    static final int GALLERY_REQUEST = 2;
    final int ACTIVITY_CHOOSE_FILE = 1;
    Bitmap bitmap = null;
    List <Bitmap> bitmapArray = new ArrayList<Bitmap>();
    Intent intentAvatar = new Intent();
    final int takeFlags = intentAvatar.getFlags()
            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        btnSave = (Button) findViewById(R.id.button_save);
        btnDelete = (Button) findViewById(R.id.button_delete);
        btnCall = (Button) findViewById(R.id.button_call);
        btnAvatar = (ImageButton)  findViewById(R.id.imageButtonEdit);
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
        phoneBook = new PhoneBook("", Long.getLong("0"), "");
        int permissionStatus = ContextCompat.checkSelfPermission(EditActivity.this, Manifest.permission.CALL_PHONE);
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
                if(ContextCompat.checkSelfPermission(EditActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED) {
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
                name = namefield.getText().toString();
                phonenumber = Long.parseLong(phonefield.getText().toString());
                email = emailfield.getText().toString();
                ContentValues cv = new ContentValues();
                cv.put(PhoneContract.PhoneEntry.COLUMN_NAME, name);
                cv.put(PhoneContract.PhoneEntry.COLUMN_PHONENUMBER, phonenumber);
                cv.put(PhoneContract.PhoneEntry.COLUMN_EMAIL, email);
                cv.put(PhoneContract.PhoneEntry.COLUMN_IMAGEPATH, imagepath);
                db.update("phonebook", cv, "name = ? AND phonenumber = ? AND email = ?", new String[]{phoneBook.getName(), String.valueOf(phoneBook.getPhone()), phoneBook.getEmail()});
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        };
        btnSave.setOnClickListener(oclBtnSave);
        //Удалить
        View.OnClickListener oclBtnDelete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber = Long.parseLong(phonefield.getText().toString());
                db.delete("phonebook","phonenumber = ? AND name= ? AND email= ?", new String[] {phoneBook.getPhone().toString(),phoneBook.getName(),phoneBook.getEmail()});
                // установка текста элемента TextView
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
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

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private String saveToInternalStorage(Bitmap bitmapImage,String name){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,name);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
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