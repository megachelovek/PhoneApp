package com.example.danilius.phoneapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.TextView;

import com.example.danilius.phoneapp.Activity.IAddServrerActivityCallback;
import com.example.danilius.phoneapp.Activity.IClientActivityCallback;
import com.example.danilius.phoneapp.Activity.IEditServerActivityCallback;
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
    IClientActivityCallback clientActivityCallback;
    IAddServrerActivityCallback addServrerActivityCallback;
    IEditServerActivityCallback editServerActivityCallback;
    int port = 8080;
    String action;
    Socket socket;

    PhoneBook phoneBook,newPhoneBook;

    public Client(String addr, int port, TextView textResponse, Activity delegate,IClientActivityCallback callback) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        tdelegate = delegate;
        this.clientActivityCallback =callback;
        action = "GETALL";
     }
    public Client(String addr, int port,IAddServrerActivityCallback callback,String action_activity,PhoneBook phoneBook_activity) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        this.addServrerActivityCallback =callback;
        action=action_activity;
        phoneBook = phoneBook_activity;
    }
    public Client(String addr, int port, IEditServerActivityCallback callback, String action_activity, PhoneBook phoneBook_activity) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        this.editServerActivityCallback =callback;
        action=action_activity;
        phoneBook = phoneBook_activity;
    }
    public Client(String addr, int port, IEditServerActivityCallback callback, String action_activity, PhoneBook phoneBook_activity, PhoneBook new_phoneBook_activity) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        this.editServerActivityCallback =callback;
        newPhoneBook = new_phoneBook_activity;
        action=action_activity;
        phoneBook = phoneBook_activity;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(Void... arg0) {
        socket = null;
        switch (action){
            case "GETALL": {
                try {
                    socket = new Socket(dstAddress, dstPort);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    RequestPhoneApp.GETALL(dos);
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    File = dataInputStream.readUTF();
                } catch (UnknownHostException e) {
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
            }break;
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
            }break;

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
            }break;
            case "EDIT":{
                try {
                    socket = new Socket(dstAddress, dstPort);
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                    RequestPhoneApp.EDIT(dos,phoneBook,newPhoneBook);
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
            }break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        switch (action) {
            case "GETALL": {
                TextView msg = tdelegate.findViewById(R.id.textview_file);
                msg.setText(File);
                clientActivityCallback.callingBackClientActivity();
            }break;
            case "ADD": {
                addServrerActivityCallback.callingBackAddServerActivity();
            }break;
            case "DELETE": {
                editServerActivityCallback.callingBackEditServerActivity();
            }break;
            case "EDIT": {
                editServerActivityCallback.callingBackEditServerActivity();
            }break;

        }
    }


}