package com.example.danilius.phoneapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.example.danilius.phoneapp.Activity.IClientCallback;
import com.example.danilius.phoneapp.data.RequestPhoneApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xml.sax.Parser;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, String> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    String File;
    Activity tdelegate;
    IClientCallback callback_async;
    RestService service;
    int port = 8080;
    Socket socket;
    String action = "GETALL";
    PhoneBook phoneBook;

    public Client(String addr, int port, TextView textResponse, Activity delegate,IClientCallback callback) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        tdelegate = delegate;
        this.callback_async =callback;
     }
    public Client(String addr, int port,IClientCallback callback,String action_activity,PhoneBook phoneBook_activity) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        this.callback_async =callback;
        action=action_activity;
        phoneBook = phoneBook_activity;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... arg0) {
        socket = null;
        switch (action){
            case "GETALL":{
                try {
                    socket = new Socket(dstAddress, dstPort);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    RequestPhoneApp.GETALL(dos);
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    File = dataInputStream.readUTF();
                }
                catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "IOException: " + e.toString();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
            case "ADD":{
                try {
                    socket = new Socket(dstAddress, dstPort);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    RequestPhoneApp.ADD(dos,phoneBook);
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    File = dataInputStream.readUTF();
                }
                catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "IOException: " + e.toString();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }

            case "DELETE":{
                try {
                    socket = new Socket(dstAddress, dstPort);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    RequestPhoneApp.DELETE(dos,phoneBook);
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    File = dataInputStream.readUTF();
                }
                catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "UnknownHostException: " + e.toString();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    response = "IOException: " + e.toString();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        if (action == "GETALL")
        {
        TextView msg = tdelegate.findViewById(R.id.textview_file);
        msg.setText(File);
        callback_async.callingBack();
        }
        else {
            callback_async.callingBack();
        }
    }


}