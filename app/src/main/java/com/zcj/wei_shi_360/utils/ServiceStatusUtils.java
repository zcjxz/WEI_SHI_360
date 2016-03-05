package com.zcj.wei_shi_360.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

//服务状态工具类
public class ServiceStatusUtils {
    public static boolean isServiceRunning(Context context,String servicesName){
        boolean isRunning=false;
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取系统运行的服务
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo runningServicesInfo :runningServices) {
            String className = runningServicesInfo.service.getClassName();
            if (className.equals(servicesName)){
                isRunning=true;
                break;
            }
        }
        return isRunning;
    }
}
