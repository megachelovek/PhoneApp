package com.example.danilius.phoneapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.danilius.phoneapp.data.PhoneContract.PhoneEntry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PhoneAppDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PhoneAppDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "PhoneApp.db";
    private static final String DATABASE_TABLE = "phonebook";
    private static  String DATABASE_PATH = "";
    public final static String _ID = "_ID";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_PHONENUMBER = "phonenumber";
    public final static String COLUMN_EMAIL = "email";
    private boolean mNeedUpdate = false;
    private final Context mContext;

    private static final int DATABASE_VERSION = 1;

    public PhoneAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        this.getReadableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PHONEBOOK_TABLE = "CREATE TABLE " + PhoneEntry.TABLE_NAME + " ("
                + PhoneEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PhoneEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + PhoneEntry.COLUMN_PHONENUMBER + " INTEGER NOT NULL DEFAULT 0,"
                + PhoneEntry.COLUMN_EMAIL + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_PHONEBOOK_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        // Создаём новую таблицу
        onCreate(db);
    }

    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
            if (dbFile.exists())
                dbFile.delete();

            copyDataBase();

            mNeedUpdate = false;
        }
    }
    private void copyDataBase() {
        if (!checkDataBase()) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDBFile();
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }
    private boolean checkDataBase() {
        File dbFile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbFile.exists();
    }
    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

}