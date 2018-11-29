package com.example.danilius.phoneapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danilius.phoneapp.PhoneBook;
import com.example.danilius.phoneapp.PhoneBookAdapter;
import com.example.danilius.phoneapp.R;
import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class MainActivity extends AppCompatActivity {


    private ListView lvPhone;
    private TextView selection;
    private ArrayList<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private Button btnAdd,btnCall,btnClient;
    private static final int READ_REQUEST_CODE = 42;
    Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selection = (TextView) findViewById(R.id.selection);
        lvPhone = (ListView) findViewById(R.id.listPhone);
        btnAdd = (Button) findViewById(R.id.button_add);
        btnCall = (Button) findViewById(R.id.button_call);
        btnClient = (Button) findViewById(R.id.button_sync);

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
                listPhoneBook.add(new PhoneBook(c.getString(1), c.getLong(2), c.getString(3),c.getString(4)));
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

        //КНОПКА СИНХРОНИЗАЦИИ
        View.OnClickListener oclBtnClient = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                String listGson = gson.toJson(listPhoneBook);
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                intent.putExtra("MainListPhoneBook", listGson);
                startActivity(intent);
            }
        };
        btnClient.setOnClickListener(oclBtnClient);
    }



}
