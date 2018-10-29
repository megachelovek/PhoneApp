package com.example.danilius.phoneapp;

public class CallbackClass {

    IClientCallback callback;

    interface IClientCallback {
        void callingBack();
    }

    public void registerCallBack(IClientCallback callback){
        this.callback = callback;
    }

}
