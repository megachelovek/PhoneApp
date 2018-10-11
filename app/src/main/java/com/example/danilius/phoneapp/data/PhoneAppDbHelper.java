package com.example.danilius.phoneapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.util.Log;

import com.example.danilius.phoneapp.data.PhoneContract.PhoneEntry;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.example.danilius.phoneapp.data.PhoneContract.PhoneEntry.TABLE_NAME;

public class PhoneAppDbHelper extends SQLiteOpenHelper {
    final static int DB_VER = 1;
    public static final String LOG_TAG = PhoneAppDbHelper.class.getSimpleName();
    private static final String DB_NAME  = "PhoneApp.db";
    private static final String TABLE_NAME  = "phonebook";
    private static  String DATABASE_PATH = "";
    final String DEBUG_TAG = "PhoneApp";
    public final static String _ID = "_ID";
    public final static String COLUMN_NAME = "name";
    public final static String COLUMN_PHONENUMBER = "phonenumber";
    public final static String COLUMN_EMAIL = "email";
    final String DATA_FILE_NAME = "data.txt";
    final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
            "( _id INTEGER PRIMARY KEY , "+
            " todo TEXT)";
    final String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;
    private boolean mNeedUpdate = false;
    private final Context mContext;

    private static final int DATABASE_VERSION = 1;

    public PhoneAppDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17)
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(DEBUG_TAG,"onCreate() called");
        db.execSQL(CREATE_TABLE);
        fillData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    private ArrayList<String> getData() {
        InputStream stream = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            stream = mContext.getAssets().open(DATA_FILE_NAME);
        }
        catch (IOException e) {
            Log.d(DEBUG_TAG,e.getMessage());
        }

        DataInputStream dataStream = new DataInputStream(stream);
        String data = "";
        try {
            while( (data=dataStream.readLine()) != null ) {
                list.add(data);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    private void fillData(SQLiteDatabase db){
        ArrayList<String> data = getData();
        for(String dt:data) Log.d(DEBUG_TAG,"item="+dt);

        if( db != null ){
            ContentValues values;

            for(String dat:data){
                values = new ContentValues();
                values.put("todo", dat);
                db.insert(TABLE_NAME, null, values);
            }
        }
        else {
            Log.d(DEBUG_TAG,"db null");
        }
    }
    public void updateDataBase() throws IOException {
        if (mNeedUpdate) {
            File dbFile = new File(DATABASE_PATH + DB_NAME);
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
        File dbFile = new File(DATABASE_PATH + DB_NAME);
        return dbFile.exists();
    }
    private void copyDBFile() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        //InputStream mInput = mContext.getResources().openRawResource(R.raw.info);
        OutputStream mOutput = new FileOutputStream(DATABASE_PATH + DB_NAME);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0)
            mOutput.write(mBuffer, 0, mLength);
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

}