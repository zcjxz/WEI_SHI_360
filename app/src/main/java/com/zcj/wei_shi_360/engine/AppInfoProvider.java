package com.zcj.wei_shi_360.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.zcj.wei_shi_360.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZCJ on 2016/5/22.
 */
public class AppInfoProvider {
    /**
     * 获取所有安装的应用程序信息
     * @return 返回AppInfo的list
     */
    public static List<AppInfo> getAppInfos(Context context){
        PackageManager packageManager=context.getPackageManager();
        List<AppInfo> appInfos=new ArrayList<AppInfo>();
        //获取所有装在手机上的应用程序的信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo:packageInfos){
            AppInfo appInfo=new AppInfo();
            //packageInfo 相当于一个app的清单文件
            String packageName = packageInfo.packageName;
            //获取app的名称和图标
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            String name = applicationInfo.loadLabel(packageManager).toString();
            Drawable icon = applicationInfo.loadIcon(packageManager);
            int flags = applicationInfo.flags;
//            if((flags&ApplicationInfo.FLAG_SYSTEM)==0){
//                //用户程序
//                appInfo.setUserApp(true);
//            }else{
//                //系统程序
//                appInfo.setUserApp(false);
//            }
            appInfo.setUserApp((flags&ApplicationInfo.FLAG_SYSTEM)==0);
//            if ((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
//                //程序安装在手机内存
//                appInfo.setInRom(true);
//            }else{
//                //程序安装在SD卡
//                appInfo.setInRom(false);
//            }
            appInfo.setInRom((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0);
            appInfo.setIcon(icon);
            appInfo.setName(name);
            appInfo.setPackName(packageName);
            appInfos.add(appInfo);
        }
        return appInfos;
    }
}
