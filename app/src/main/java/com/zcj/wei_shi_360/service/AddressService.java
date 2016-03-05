package com.zcj.wei_shi_360.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.zcj.wei_shi_360.dbUtils.AddressUtils;


public class AddressService extends Service{

    private TelephonyManager telephonyManager;
    private MyListener listener;
    private OutCallReceiver receiver;

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
        //注册广播接收者
        receiver=new OutCallReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_NONE);//停止来电监听
        unregisterReceiver(receiver);
        receiver=null;
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
    //服务里面的内部类
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到拨出的电话号码
            String phone=getResultData();
            String address = AddressUtils.getAddress(phone);
            Toast.makeText(context, "address", Toast.LENGTH_LONG).show();
        }
    }
}
