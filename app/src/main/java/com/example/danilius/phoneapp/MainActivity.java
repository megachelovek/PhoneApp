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
    private Button btnAdd;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // получаем элемент TextView
        selection = (TextView) findViewById(R.id.selection);
        // получаем элемент ListView
        lvPhone = (ListView) findViewById(R.id.listPhone);
        btnAdd = (Button) findViewById(R.id.button_add);
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

        // создаем адаптер
        PhoneBookAdapter adapter = new PhoneBookAdapter(this, listPhoneBook);
        // устанавливаем для списка адаптер
        lvPhone.setAdapter(adapter);
        // добвляем для списка слушатель
        lvPhone.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // по позиции получаем выбранный элемент
                PhoneBook selectedItem = listPhoneBook.get(position);
                // установка текста элемента TextView
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(PhoneBook.class.getSimpleName(), selectedItem);
                startActivity(intent);
            }
        });

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM phonebook;", null);
                //db.rawQuery("INSERT INTO phonebook (name, phonenumber, email) VALUES ('', 0, '');", null);
                ContentValues cv = new ContentValues();
                cv.put(PhoneContract.PhoneEntry.COLUMN_NAME, "");
                cv.put(PhoneContract.PhoneEntry.COLUMN_PHONENUMBER, 0);
                cv.put(PhoneContract.PhoneEntry.COLUMN_EMAIL, "");
                //db.insert(PhoneContract.PhoneEntry.TABLE_NAME,null,cv);
                //db.execSQL("INSERT INTO phonebook (name, phonenumber, email) VALUES ('', 0, '');");
                listPhoneBook.add(new PhoneBook("", 0, ""));
                PhoneBook selectedItem = listPhoneBook.get(listPhoneBook.size() - 1);
                // установка текста элемента TextView
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(PhoneBook.class.getSimpleName(), selectedItem);
                startActivity(intent);
            }
        };
        btnAdd.setOnClickListener(oclBtnOk);
        View.OnClickListener oclBtnAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        };
        btnAdd.setOnClickListener(oclBtnAdd);

    }

}

