package com.example.danilius.phoneapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.example.danilius.phoneapp.data.PhoneAppDbHelper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientActivity extends AppCompatActivity implements IClient {

    private ListView lvPhone;
    private TextView selection;
    private List<PhoneBook> listPhoneBook = new ArrayList<PhoneBook>();
    private PhoneAppDbHelper dbHelper;
    private SQLiteDatabase db;
    Server server;
    Client client;
    public TextView  msg,textview_file;
    int port =8080;
    private String File ="",ip,addr;
    private Button ClientServer, ServerClient;
    EditText ipfield;
    Cursor c;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        selection = (TextView) findViewById(R.id.selection);
        lvPhone = (ListView) findViewById(R.id.listPhone);
        msg = (TextView) findViewById(R.id.textView_msg);
        textview_file =(TextView) findViewById(R.id.textview_file);
        ClientServer = (Button) findViewById(R.id.button_client_server);
        ServerClient = (Button) findViewById(R.id.button_server_client);
        ipfield = (EditText) findViewById(R.id.editText_ip);
        msg.setText(getIPAddress(true));
        Client client=new Client();

        if (ContextCompat.checkSelfPermission(ClientActivity.this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ClientActivity.this,new String[]{Manifest.permission.INTERNET},1);}


        View.OnClickListener oclBtnServerClient = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // File="localhost:8080/getall";
                //server = new Server(ClientActivity.this, File);
                client = new Client(ipfield.getText().toString(),port,msg,ClientActivity.this);
                client.execute();

            }

        };
        ServerClient.setOnClickListener(oclBtnServerClient);
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

    @Override
    public void postResult(String output){
        msg.setText(File);
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
    }
}
