package com.example.danilius.phoneapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.danilius.phoneapp.data.PhoneContract.PhoneEntry;

public class PhoneAppDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PhoneAppDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "PhoneApp.db";

    private static final int DATABASE_VERSION = 1;

    public PhoneAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


        @Override
    public void onCreate(SQLiteDatabase db) {
            String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + PhoneEntry.TABLE_NAME + " ("
                    + PhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PhoneEntry.COLUMN_NAME + " TEXT NOT NULL, "
                    + PhoneEntry.COLUMN_PHONENUMBER + " INTEGER NOT NULL DEFAULT 0,"
                    + PhoneEntry.COLUMN_EMAIL + " TEXT NOT NULL);";

            db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);
    }
}
