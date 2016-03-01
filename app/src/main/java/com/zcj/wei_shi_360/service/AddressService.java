package com.zcj.wei_shi_360.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.zcj.wei_shi_360.dbUtils.AddressUtils;

/**
 * Created by 曾灿杰 on 2016/2/20.
 */
public class AddressService extends Service{

    private TelephonyManager telephonyManager;
    private MyListener listener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//监听电话的状态
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_NONE);//停止来电监听
    }

    private class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("AAA", "onCallStateChanged: 响铃");
                    String address = AddressUtils.getAddress(incomingNumber);
                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("AAA", "onCallStateChanged: 摘机");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("AAA", "onCallStateChanged: 待机");
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
}
