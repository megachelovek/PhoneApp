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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danilius.phoneapp.Client;
import com.example.danilius.phoneapp.PhoneBook;
import com.example.danilius.phoneapp.PhoneBookAdapter;
import com.example.danilius.phoneapp.R;
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

public class ClientActivity extends AppCompatActivity implements IClientActivityCallback {

    private ListView lvPhone;
    private TextView selection;
    private List<PhoneBook> listPhoneBookServer = new ArrayList<PhoneBook>();
    private List<PhoneBook> listPhoneBookMain = new ArrayList<PhoneBook>();
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    Client client;
    public TextView  msg,textview_file;
    int port =8080;
    private String File,ip,addr;
    private ImageButton AddToClient,AddListToClientServer;
    private Button  ServerClient,AddtoServer;
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
        AddToClient = (ImageButton) findViewById(R.id.ib_AddListPhoneBooksToClient);
        AddListToClientServer = (ImageButton) findViewById(R.id.ib_AddListPhoneBooksToServer);
        AddtoServer = (Button) findViewById(R.id.b_AddPhoneBookToServer);
        ServerClient = (Button) findViewById(R.id.b_GetListFromServer);
        ipfield = (EditText) findViewById(R.id.editText_ip);
        msg.setText(getIPAddress(true));
        Bundle arguments = getIntent().getExtras();
        String listMain = arguments.getString("MainListPhoneBook");
        Gson gson = new Gson();
        Type PhoneBookListType = new TypeToken<ArrayList<PhoneBook>>(){}.getType();
        ArrayList<PhoneBook> listPhoneBookMain = gson.fromJson(listMain,PhoneBookListType);

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
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(listPhoneBookServer != null){
                    for (PhoneBook phoneBook : listPhoneBookServer) {
                        if (!containsPhoneBook(listPhoneBookMain,phoneBook)) {
                            ContentValues cv = new ContentValues();
                            cv.put(PhoneContract.PhoneEntry.COLUMN_NAME, phoneBook.getName());
                            cv.put(PhoneContract.PhoneEntry.COLUMN_PHONENUMBER, phoneBook.getPhone());
                            cv.put(PhoneContract.PhoneEntry.COLUMN_EMAIL, phoneBook.getEmail());
                            cv.put(PhoneContract.PhoneEntry.COLUMN_IMAGEPATH, phoneBook.getImagePath());
                            db.insert("phonebook", null, cv);
                            listPhoneBookMain.add(phoneBook);
                        }
                    }
                    Intent intent = new Intent(ClientActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        };
        AddToClient.setOnClickListener(oclBtnAddToClient);
        //Добавление на сервер
        View.OnClickListener oclBtnAddToServer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, AddServerActivity.class);
                intent.putExtra("ip", ipfield.getText().toString());
                intent.putExtra("port", port);
                startActivity(intent);
            }
        };
        AddtoServer.setOnClickListener(oclBtnAddToServer);
        //Нажатие на запись
        lvPhone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                PhoneBook selectedItem = listPhoneBookServer.get(position);
                Intent intent = new Intent(ClientActivity.this, EditServerActivity.class);
                intent.putExtra("ip", ipfield.getText().toString());
                intent.putExtra("port", port);
                intent.putExtra(PhoneBook.class.getSimpleName(), selectedItem);
                startActivity(intent);
            }
        });
        //ДОБАВЛЕНИЕ СПИСКА НА СЕРВЕР
        View.OnClickListener oclBtnAddListToClientServer = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String ip = ipfield.getText().toString();
                    client = new Client(ip,port, msg,ClientActivity.this , "ADDLIST",listPhoneBookMain,ClientActivity.this);
                    client.execute();
            }
        };
        AddListToClientServer.setOnClickListener(oclBtnAddListToClientServer);

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

    public void callingBackClientActivity(){
        File = textview_file.getText().toString();
        if (File!="request_completed") {
            Type type = new TypeToken<List<PhoneBook>>() {}.getType();
            listPhoneBookServer = new Gson().fromJson(File, type);
            PhoneBookAdapter adapter = new PhoneBookAdapter(this, listPhoneBookServer);
            lvPhone.setAdapter(adapter);
        }
    }

    public boolean containsPhoneBook(List<PhoneBook> list,PhoneBook phoneBook){
        for(PhoneBook phoneBookServer : list){
            if (phoneBookServer.getName().equals(phoneBook.getName())  && phoneBookServer.getPhone().equals(phoneBook.getPhone()) && phoneBookServer.getEmail().equals(phoneBook.getEmail()) ){
                return true;
            }
        }
        return false;
    }


}
