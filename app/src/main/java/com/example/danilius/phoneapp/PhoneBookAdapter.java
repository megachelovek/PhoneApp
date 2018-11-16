package com.example.danilius.phoneapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danilius.phoneapp.Activity.EditActivity;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PhoneBookAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhoneBook> mListPhoneBook;

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

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // get selected entry
        PhoneBook entry = mListPhoneBook.get(pos);

        // inflating list view layout if null
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item_layout, null);
        }
        if(ContextCompat.checkSelfPermission(mContext,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.imgAvatar);
            if ((entry.getImagePath()) == "" || (entry.getImagePath()) == null) {
                ivAvatar.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.avatar_icon));
            } else {
                Uri uri = Uri.parse(entry.getImagePath());
                ivAvatar.setImageURI(uri);
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

}