package com.example.danilius.phoneapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends AppCompatActivity {

    private ListView lvPhone;
    private TextView selection;
    private List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // получаем элемент TextView
        selection = (TextView) findViewById(R.id.selection);
        // получаем элемент ListView
        lvPhone = (ListView) findViewById(R.id.listPhone);


        listPhoneBook.add(new PhoneBook("Pete Houston", "010-9817-6331", "pete.houston.17187@gmail.com"));
        listPhoneBook.add(new PhoneBook("Lina Cheng", "046-7764-1142", "lina.cheng011@sunny.com"));
        listPhoneBook.add(new PhoneBook("Jenny Nguyen", "0913-223-498", "jenny_in_love98@yahoo.com"));

        // создаем адаптер
        PhoneBookAdapter adapter = new PhoneBookAdapter(this, listPhoneBook);
        // устанавливаем для списка адаптер
        lvPhone.setAdapter(adapter);
        // добвляем для списка слушатель
        lvPhone.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                // по позиции получаем выбранный элемент
                PhoneBook selectedItem = (PhoneBook)listPhoneBook.get(position);
                // установка текста элемента TextView
                Intent intent = new Intent(com.example.danilius.phoneapp.MainActivity.this,EditActivity.class);
                intent.putExtra(PhoneBook.class.getSimpleName(),selectedItem);
                startActivity(intent);
            }
        });
    }
}
/*
public class MainActivity extends Activity {

    private ListView lvPhone;
    private TextView selection;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selection = (TextView) findViewById(R.id.selection);
        lvPhone = (ListView)findViewById(R.id.listPhone);

        List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
        listPhoneBook.add(new PhoneBook("Pete Houston", "010-9817-6331", "pete.houston.17187@gmail.com"));
        listPhoneBook.add(new PhoneBook(

                "Lina Cheng", "046-7764-1142", "lina.cheng011@sunny.com"));
        listPhoneBook.add(new PhoneBook(

                "Jenny Nguyen", "0913-223-498", "jenny_in_love98@yahoo.com"));
        PhoneBookAdapter adapter = new PhoneBookAdapter(this, listPhoneBook);
        lvPhone.setAdapter(adapter);

        // добвляем для списка слушатель
        listPhoneBook.setOnItemClickListener{


            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // по позиции получаем выбранный элемент
                String selectedItem = lvPhone[position];
                // установка текста элемента TextView
                selection.setText(selectedItem);
            }
        }
}*/
