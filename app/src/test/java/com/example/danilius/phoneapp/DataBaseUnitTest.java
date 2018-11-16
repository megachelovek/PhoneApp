package com.example.danilius.phoneapp;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.danilius.phoneapp.data.PhoneAppDbHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class DataBaseUnitTest {
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    private List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    private Cursor c;
    private Context context;

    @Before
    public void setUp() throws Exception {
        dbHelper = new PhoneAppDbHelper(context);
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
                listPhoneBook.add(new PhoneBook(c.getString(1), c.getLong(2), c.getString(3)));
            }
            while (c.moveToNext());
        }
        c.close();
    }

    @Test
    public void testSum() throws Exception {
        assertTrue(listPhoneBook.isEmpty());
    }

    @After
    public void tearDown() throws Exception {
        listPhoneBook = null;
    }

}
