package com.zcj.wei_shi_360.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zcj.wei_shi_360.dbUtils.BlackNumberDao;

import java.lang.reflect.Method;

public class CallSafeService extends Service {
    public CallSafeService() {
    }
    private  InnerSmsReceiver receiver;
    private BlackNumberDao dao;
    private TelephonyManager tm;
    private PhoneStateListener listener;
    @Override
    public void onCreate() {
        dao=new BlackNumberDao(this);
        tm=(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        listener=new MyListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        receiver=new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(1000);
        registerReceiver(receiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        tm.listen(listener,PhoneStateListener.LISTEN_NONE);
        tm=null;
        super.onDestroy();
    }

    private class InnerSmsReceiver extends BroadcastReceiver{
        public InnerSmsReceiver(){
            Log.i("InnerSmsReceiver:","已经启动");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("InnerSmsReceiver:","收到信息");
            Object[] objs = (Object[])intent.getExtras().get("pdus");
            for (Object obj:objs){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                //找到发件人
                String sender = smsMessage.getOriginatingAddress();
                String mode = dao.findNumber(sender);
                if ("2".equals(mode)||"3".equals(mode)){
                    abortBroadcast();
                    Log.i("InnerSmsReceiver","拦截到短信");
                }
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE://待机
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK://摘记
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃
                    String mode=dao.findNumber(incomingNumber);
                    if ("1".equals(mode)||"3".equals(mode)){
                        endCall();//有问题，未实现
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

        private void endCall() {
            try {
                Class clazz=CallSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
                Method method=clazz.getDeclaredMethod("getService",String.class);
                IBinder iBinder= (IBinder) method.invoke(null,TELECOM_SERVICE);
//                报错，没法挂断电话
//                ITelephony.Stub.asInterface(iBinder).endcall();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
