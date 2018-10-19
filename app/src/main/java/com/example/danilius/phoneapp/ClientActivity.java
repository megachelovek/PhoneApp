package com.example.danilius.phoneapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danilius.phoneapp.data.PhoneAppDbHelper;

import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientActivity extends AppCompatActivity {

    private ListView lvPhone;
    private TextView selection;
    private List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    Cursor c;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        selection = (TextView) findViewById(R.id.selection);
        lvPhone = (ListView) findViewById(R.id.listPhone);

        List<String> names;

        try (ServerSocket serverSocket = new ServerSocket(2121)) {
            try (Socket socket = serverSocket.accept();
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
                URI uri = new URI("localhost:2121/getAll");
                out.writeObject(uri);
            }
            catch (IOException exc) {
                exc.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        catch (IOException exc) {
            exc.printStackTrace();
        }

        try (Socket socket = new Socket("192.168.1.34", 2121)) {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            try {
                JSONArray jsonArray = (JSONArray) in.readObject();
            } catch (ClassNotFoundException exc) {
                exc.printStackTrace();
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    } catch (UnknownHostException exc) {
        exc.printStackTrace();
    } catch (IOException exc) {
        exc.printStackTrace();
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
                Intent intent = new Intent(ClientActivity.this, EditActivity.class);
                intent.putExtra(PhoneBook.class.getSimpleName(), selectedItem);
                startActivity(intent);
            }
        });



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