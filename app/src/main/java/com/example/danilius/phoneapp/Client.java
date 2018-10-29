package com.example.danilius.phoneapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends AsyncTask<Void, Void, String> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    String File;
    Activity tdelegate;
    CallbackClass callback = new CallbackClass();


    public Client(String addr, int port, TextView textResponse, Activity delegate) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        tdelegate = delegate;

    }
    @Override
    protected String doInBackground(Void... arg0) {
        Socket socket = null;
        try {
            socket = new Socket(dstAddress, dstPort);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];
            int bytesRead;
            InputStream inputStream = socket.getInputStream();
            /*
             * notice: inputStream.read() will block if no data return
             */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                File = byteArrayOutputStream.toString("UTF-8");
                if (File != "") {
                    response = "File ok";
                }
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
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
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        callback.registerCallBack((CallbackClass.IClientCallback) tdelegate);
        TextView msg = tdelegate.findViewById(R.id.textview_file);
        msg.setText(File);
       //сделать вызов callback !!!!!!!!!

    }

}