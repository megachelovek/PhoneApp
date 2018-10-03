package com.example.danilius.phoneapp;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PackageManager pm = getPackageManager();
        boolean isTelephonySupported = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        boolean isGSMSupported = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_GSM);
        TelephonyManager.listen(stateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
    PhoneStateListener stateListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: break;
                case TelephonyManager.CALL_STATE_OFFHOOK: break;
                case TelephonyManager.CALL_STATE_RINGING:
                    doMagicWork(incomingNumber); // Поступил звонок с номера incomingNumber
                    break;
            }
        }
    };

}
