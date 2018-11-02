package com.example.danilius.phoneapp.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danilius.phoneapp.Client;
import com.example.danilius.phoneapp.Activity.IClientCallback;
import com.example.danilius.phoneapp.PhoneBook;
import com.example.danilius.phoneapp.PhoneBookAdapter;
import com.example.danilius.phoneapp.R;
import com.example.danilius.phoneapp.Server;
import com.example.danilius.phoneapp.data.PhoneAppDbHelper;
import com.example.danilius.phoneapp.data.PhoneContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientActivity extends AppCompatActivity implements IClientCallback {

    private ListView lvPhone;
    private TextView selection;
    private List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    Server server;
    Client client;
    public TextView  msg,textview_file;
    int port =8080;
    private String File,ip,addr;
    private Button ClientServer, ServerClient,AddToClient;
    EditText ipfield;
    Cursor c;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        selection = (TextView) findViewById(R.id.selection);
        lvPhone = (ListView) findViewById(R.id.listPhoneClient);
        msg = (TextView) findViewById(R.id.textView_msg);
        textview_file =(TextView) findViewById(R.id.textview_file);
        AddToClient = (Button) findViewById(R.id.add_in_DB);
        ClientServer = (Button) findViewById(R.id.button_client_server);
        ServerClient = (Button) findViewById(R.id.button_server_client);
        ipfield = (EditText) findViewById(R.id.editText_ip);
        msg.setText(getIPAddress(true));

        dbHelper = new PhoneAppDbHelper(this);
        try {
            dbHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }


        if (ContextCompat.checkSelfPermission(ClientActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ClientActivity.this,new String[]{Manifest.permission.INTERNET},1);}

        //Получить список с сервера на устройство
        View.OnClickListener oclBtnServerClient = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = new Client(ipfield.getText().toString(),port,msg,ClientActivity.this,ClientActivity.this);
                client.execute();
            }

        };
        ServerClient.setOnClickListener(oclBtnServerClient);

        //Добавить записи с сервера на устройство
        View.OnClickListener oclBtnAddToClient = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listPhoneBook != null){
                    for (PhoneBook phoneBook : listPhoneBook) {
                        ContentValues cv = new ContentValues();
                        cv.put(PhoneContract.PhoneEntry.COLUMN_NAME, phoneBook.getName());
                        cv.put(PhoneContract.PhoneEntry.COLUMN_PHONENUMBER, phoneBook.getPhone());
                        cv.put(PhoneContract.PhoneEntry.COLUMN_EMAIL, phoneBook.getPhone());
                        db.insert("phonebook",null,cv);
                    }
                    Intent intent = new Intent(ClientActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        };
        AddToClient.setOnClickListener(oclBtnAddToClient);
        
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    public void callingBack(){
        File = textview_file.getText().toString();
        if (File!="request_completed") {
            Type type = new TypeToken<List<PhoneBook>>() {
            }.getType();
            listPhoneBook = new Gson().fromJson(File, type);
            PhoneBookAdapter adapter = new PhoneBookAdapter(this, listPhoneBook);
            lvPhone.setAdapter(adapter);
        }
    }

}
