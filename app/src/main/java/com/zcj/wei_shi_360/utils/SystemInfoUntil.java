package com.zcj.wei_shi_360.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 系统信息的工具类
 */
public class SystemInfoUntil {
    /**
     * 获取系统正在运行的进程数量
     * @return
     */
    public static int getRuningProcessCount(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
        return infos.size();
    }

    /**
     * 获取手机可用内存
     * @return
     */
    public static long getAvailMem(Context context){
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        return info.availMem;
    }

    /**
     * 获取手机总内存
     */
    public static long getTotalMem(Context context){
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
//        return info.totalMem;//4.0   api   totalMen 才有此属性
        File file=new File("/proc/meminfo");
        StringBuffer sb=new StringBuffer();
        try {
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));
            String line=br.readLine();
            for(char c:line.toCharArray()){
                if (c>='0'&&c<='9'){
                    sb.append(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.parseLong(sb.toString())*1024;
    }
}
