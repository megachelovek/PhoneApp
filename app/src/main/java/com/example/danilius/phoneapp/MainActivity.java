package com.example.danilius.phoneapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.example.danilius.phoneapp.data.PhoneContract;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lvPhone;
    private TextView selection;
    private List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    private PhoneAppDbHelper PaDbHelper;

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
        lvPhone.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // по позиции получаем выбранный элемент
                PhoneBook selectedItem = (PhoneBook) listPhoneBook.get(position);
                // установка текста элемента TextView
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(PhoneBook.class.getSimpleName(), selectedItem);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_new_data:
                // Пока ничего не делаем
                return true;
            case R.id.action_delete_all_entries:
                // Пока ничего не делаем
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayDatabaseInfo() {
        // Создадим и откроем для чтения базу данных
        SQLiteDatabase db = PaDbHelper.getReadableDatabase();

        // Зададим условие для выборки - список столбцов
        String[] projection = {
                PhoneContract.PhoneEntry._ID,
                PhoneContract.PhoneEntry.COLUMN_NAME,
                PhoneContract.PhoneEntry.COLUMN_PHONENUMBER,
                PhoneContract.PhoneEntry.COLUMN_EMAIL};

        // Делаем запрос
        Cursor cursor = db.query(
                PhoneContract.PhoneEntry.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        TextView displayTextView = (TextView) findViewById(R.id.textView1);

        try {
            displayTextView.setText("Таблица содержит " + cursor.getCount() + " гостей.\n\n");
            displayTextView.append(PhoneContract.PhoneEntry._ID + " - " +
                    PhoneContract.PhoneEntry.COLUMN_NAME + " - " +
                    PhoneContract.PhoneEntry.COLUMN_CITY + " - " +
                    GuestEntry.COLUMN_GENDER + " - " +
                    GuestEntry.COLUMN_AGE + "\n");

            // Узнаем индекс каждого столбца
            int idColumnIndex = cursor.getColumnIndex(GuestEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(GuestEntry.COLUMN_NAME);
            int cityColumnIndex = cursor.getColumnIndex(GuestEntry.COLUMN_CITY);
            int genderColumnIndex = cursor.getColumnIndex(GuestEntry.COLUMN_GENDER);
            int ageColumnIndex = cursor.getColumnIndex(GuestEntry.COLUMN_AGE);

            // Проходим через все ряды
            while (cursor.moveToNext()) {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentCity = cursor.getString(cityColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentAge = cursor.getInt(ageColumnIndex);
                // Выводим значения каждого столбца
                displayTextView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentCity + " - " +
                        currentGender + " - " +
                        currentAge));
            }
        } finally {
            // Всегда закрываем курсор после чтения
            cursor.close();
        }

    }
}
