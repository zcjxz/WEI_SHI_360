package com.zcj.wei_shi_360.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.receiver.MyAppWidget;
import com.zcj.wei_shi_360.utils.SystemInfoUntil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private Timer timer;
    private TimerTask task;
    private AppWidgetManager awm;
    private ScreenOffReceiver screenOffReceiver;
    private ScreenOnReceiver screenOnReceiver;

    public MyService() {
    }

    @Override
    public void onCreate() {
        screenOffReceiver = new ScreenOffReceiver();
        registerReceiver(screenOffReceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        screenOnReceiver = new ScreenOnReceiver();
        registerReceiver(screenOnReceiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
        startTimer();
        super.onCreate();
    }

    private void stopTimer(){
        if (timer!=null&&task!=null) {
            timer.cancel();
            task.cancel();
            timer = null;
            task = null;
        }
    }
    @Override
    public void onDestroy() {
        unregisterReceiver(screenOffReceiver);
        unregisterReceiver(screenOnReceiver);
        stopTimer();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private class ScreenOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            stopTimer();
            Log.i("ScreenOffReceiver", "onReceive: 锁屏了");
        }
    }
    private class ScreenOnReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            startTimer();
            Log.i("ScreenOffReceiver", "onReceive: 解锁屏幕了");
        }
    }

    private void startTimer() {
        if (timer==null&&task==null) {
            awm = AppWidgetManager.getInstance(MyService.this);
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    Log.i("timer", "run: 更新widget了");
                    //设置更新组件
                    ComponentName componentName = new ComponentName(MyService.this, MyAppWidget.class);
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.my_app_widget);
                    views.setTextViewText(R.id.appwidget_runing, "正在运行：" + SystemInfoUntil.getRuningProcessCount(MyService.this));
                    long availMem = SystemInfoUntil.getAvailMem(MyService.this);
                    views.setTextViewText(R.id.appwidget_availmem, "可用内存：" + Formatter.formatFileSize(MyService.this, availMem));
                    //描述一个动作，这个动作是由另一个应用执行的
                    Intent intent = new Intent();
                    intent.setAction("com.zcj.wei_shi_360.killProcess");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    views.setOnClickPendingIntent(R.id.widget_clean, pendingIntent);
                    awm.updateAppWidget(componentName, views);
                }
            };
            timer.schedule(task, 0, 3000);
        }
    }
}
