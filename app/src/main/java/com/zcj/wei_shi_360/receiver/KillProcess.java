package com.zcj.wei_shi_360.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

public class KillProcess extends BroadcastReceiver {

    private ActivityManager am;

    public KillProcess() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("onReceive", "收到广播了，正在清理后台进程");
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info:runningAppProcesses){
            am.killBackgroundProcesses(info.processName);
        }
    }
}
