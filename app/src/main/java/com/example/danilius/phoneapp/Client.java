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
    private IClientCallback callback_async;


    public Client(String addr, int port, TextView textResponse, Activity delegate,IClientCallback callback) {
        dstAddress = addr;
        dstPort = port;
        this.textResponse = textResponse;
        tdelegate = delegate;
        this.callback_async =callback;

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
        TextView msg = tdelegate.findViewById(R.id.textview_file);
        msg.setText(File);
        callback_async.callingBack();
    }

}