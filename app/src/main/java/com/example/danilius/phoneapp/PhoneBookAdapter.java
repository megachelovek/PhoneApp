package com.example.danilius.phoneapp;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danilius.phoneapp.Activity.EditActivity;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Provider;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static com.example.danilius.phoneapp.SaveLoadImage.getSDCardPath;

public class PhoneBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhoneBook> mListPhoneBook;
    Intent chooseFile;


    public PhoneBookAdapter(Context context, List<PhoneBook> list) {
        mContext = context;
        mListPhoneBook = list;
    }

    @Override
    public int getCount() {
        return mListPhoneBook.size();
    }

    @Override
    public Object getItem(int pos) {
        return mListPhoneBook.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // get selected entry
        PhoneBook entry = mListPhoneBook.get(pos);

        // inflating list view layout if null
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item_layout, null);
        }
        if(ContextCompat.checkSelfPermission(mContext,READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{READ_EXTERNAL_STORAGE},1);
        }

        if(ContextCompat.checkSelfPermission(mContext,READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);

            if ((entry.getImagePath()) == "" || (entry.getImagePath() == null) ||(entry.getImagePath().isEmpty())) {
                ivAvatar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.avatar_icon));
            } else {
                Bitmap test;
                try {
                    test = getBitmapFromUri(Uri.parse(entry.getImagePath()));
                    ivAvatar.setImageBitmap(test);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivAvatar.setImageURI(Uri.parse(entry.getImagePath()));
            }
        }



       // set name
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(entry.getName());

        // set phone
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
        if (entry.getPhone() != null){tvPhone.setText(entry.getPhone().toString());}


        // set email
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        tvEmail.setText(entry.getEmail());



        return convertView;
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                mContext.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


}