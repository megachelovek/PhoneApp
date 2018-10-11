package com.example.danilius.phoneapp;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.example.danilius.phoneapp.data.PhoneContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvPhone;
    private TextView selection;
    private List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private Button btnAdd,btnCall;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selection = (TextView) findViewById(R.id.selection);
        lvPhone = (ListView) findViewById(R.id.listPhone);
        btnAdd = (Button) findViewById(R.id.button_add);
        btnCall = (Button) findViewById(R.id.button_call);

        dbHelper = new PhoneAppDbHelper(this);
        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }

        c = db.rawQuery("SELECT * FROM phonebook;", null);
        if (c.moveToFirst()) {
            do {
                listPhoneBook.add(new PhoneBook(c.getString(1), c.getInt(2), c.getString(3)));
            }
            while (c.moveToNext());
        }
        c.close();

        PhoneBookAdapter adapter = new PhoneBookAdapter(this, listPhoneBook);
        lvPhone.setAdapter(adapter);

        // НАЖАТИЕ НА ЭЛЕМЕНТ СПИСКА
        lvPhone.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                PhoneBook selectedItem = listPhoneBook.get(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(PhoneBook.class.getSimpleName(), selectedItem);
                startActivity(intent);
            }
        });

        //КНОПКА ДОБАВЛЕНИЯ
        View.OnClickListener oclBtnAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        };
        btnAdd.setOnClickListener(oclBtnAdd);

        //КНОПКА ЗВОНКА
        View.OnClickListener oclBtnCall = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CallActivity.class);
                startActivity(intent);
            }
        };
        btnCall.setOnClickListener(oclBtnCall);
    }

}

/*View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM phonebook;", null);
                ContentValues cv = new ContentValues();
                cv.put(PhoneContract.PhoneEntry.COLUMN_NAME, "");
                cv.put(PhoneContract.PhoneEntry.COLUMN_PHONENUMBER, 0);
                cv.put(PhoneContract.PhoneEntry.COLUMN_EMAIL, "");

                listPhoneBook.add(new PhoneBook("", 0, ""));
                PhoneBook selectedItem = listPhoneBook.get(listPhoneBook.size() - 1);

                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(PhoneBook.class.getSimpleName(), selectedItem);
                startActivity(intent);
            }
        };
        btnAdd.setOnClickListener(oclBtnOk);*/