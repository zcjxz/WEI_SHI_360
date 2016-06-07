package com.zcj.wei_shi_360.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class AutoCleanService extends Service {

    private ScreenOffReceiver receiver;
    private ActivityManager am;

    public AutoCleanService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        receiver = new ScreenOffReceiver();
        registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (receiver!=null){
            unregisterReceiver(receiver);
            receiver=null;
        }
        super.onDestroy();
    }
    private class ScreenOffReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo info:processes){
                am.killBackgroundProcesses(info.processName);
            }
            Log.i("ScreenOffReceiver", "onReceive: 锁屏了");
        }
    }
}
