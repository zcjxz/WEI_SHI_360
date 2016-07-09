package com.zcj.wei_shi_360.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.zcj.wei_shi_360.activity.EnterPasswordActivity;
import com.zcj.wei_shi_360.dbUtils.AppLockDao;

import java.util.List;

public class WatchDogService extends Service {

    private ActivityManager am;
    private boolean flag;
    private AppLockDao dao;
    private InnerReceiver innerReceiver;
    private ScreenOffReceiver screenOffReceiver;
    private ScreenOnReceiver screenOnReceiver;
    private String tempStopProtectPackName;
    private WatchDogService.watchDogThread watchDogThread;

    private List<String> protectPackName;
    private Intent intent;
    private DataChangeRecevier dataChangeRecevier;

    public WatchDogService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        innerReceiver = new InnerReceiver();
        screenOffReceiver=new ScreenOffReceiver();
        screenOnReceiver=new ScreenOnReceiver();
        dataChangeRecevier = new DataChangeRecevier();
        registerReceiver(dataChangeRecevier,new IntentFilter("com.zcj.mobileSafe.appLockChange"));
        registerReceiver(innerReceiver,new IntentFilter("com.zcj.wei_shi_360.tempStop"));
        registerReceiver(screenOffReceiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));
        registerReceiver(screenOnReceiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
        //当前应用需要保护，弹出输入密码界面
        intent = new Intent(WatchDogService.this,EnterPasswordActivity.class);
        //服务是没有任务栈信息的，在服务开启activity，要指定这个activity的运行的任务栈
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        dao=new AppLockDao(this);
        protectPackName=dao.findAll();
        flag=true;
        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        watchDogThread = new watchDogThread();
        watchDogThread.start();
        super.onCreate();
    }
    private class watchDogThread extends Thread{
        @Override
        public void run() {
            while(flag){
                List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(1);
                ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
                String packageName = runningTaskInfo.topActivity.getPackageName();
//                if (dao.find(packageName)){   //查询数据库太慢了，消耗资源，改成查询内存
                if (protectPackName.contains(packageName)){    //查询内存，提高了效率
                    //判断是否需要临时停止保护
                    if (packageName.equals(tempStopProtectPackName)){

                    }else{

                        //设置要保护的包名
                        intent.putExtra("packName",packageName);
                        startActivity(intent);
                    }
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (innerReceiver!=null){
            unregisterReceiver(innerReceiver);
            innerReceiver=null;
        }
        if (screenOffReceiver!=null){
            unregisterReceiver(screenOffReceiver);
            screenOffReceiver=null;
        }
        if (screenOnReceiver!=null){
            unregisterReceiver(screenOnReceiver);
            screenOnReceiver=null;
        }
        if (dataChangeRecevier!=null){
            unregisterReceiver(dataChangeRecevier);
            dataChangeRecevier=null;
        }
        flag=false;
        watchDogThread.interrupt();
        watchDogThread=null;
        tempStopProtectPackName=null;
        super.onDestroy();
    }

    private class InnerReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("onReceive:","接收到了临时停止保护广播");
            tempStopProtectPackName = intent.getStringExtra("packName");
        }
    }
    private class ScreenOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            flag=false;
            watchDogThread.interrupt();
            watchDogThread=null;
            tempStopProtectPackName=null;
            Log.i("ScreenOffReceiver", "onReceive: 锁屏了");
        }
    }
    private class ScreenOnReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            flag=true;
            if (watchDogThread==null){
                watchDogThread=new watchDogThread();
                watchDogThread.start();
            }
            Log.i("ScreenOffReceiver", "onReceive: 解锁屏幕了");
        }
    }
    private class DataChangeRecevier extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("DataChangeRecevier", "数据库内容变化了");
            protectPackName=dao.findAll();
        }
    }
}
