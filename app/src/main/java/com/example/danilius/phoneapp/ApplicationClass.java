package com.example.danilius.phoneapp;

import android.app.Application;

public class ApplicationClass extends Application {
    private String FileResponse;
    public  ApplicationClass(){}
    public String getFileResponse() {
        return this.FileResponse;
    }
    public void setFileResponse(String File) {
       this.FileResponse = File;
    }
}
