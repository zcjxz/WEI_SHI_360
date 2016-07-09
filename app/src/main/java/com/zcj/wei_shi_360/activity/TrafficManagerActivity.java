package com.zcj.wei_shi_360.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zcj.wei_shi_360.R;

import java.util.List;

public class TrafficManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_manager);
        //1.获取包管理器
        PackageManager pm=getPackageManager();
        //2.遍历所有应用的uid
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo:applicationInfos){
            int uid=applicationInfo.uid;
            //proc/uid_stat/10086
            //uid为1000表示为系统底层应用
            long tx = TrafficStats.getUidTxBytes(uid);//发送或上传的流量，单位Bytes
            long rt = TrafficStats.getUidRxBytes(uid);//下载的流量，单位Bytes
            //方法返回值如果为-1，代表应用程序没有产生流量或系统不支持流量统计
        }
        TrafficStats.getMobileRxBytes();//手机移动网络下载的总流量
        TrafficStats.getMobileTxBytes();//手机移动网络上传的总流量
        TrafficStats.getTotalTxBytes();//手机wifi，移动网络上传的总流量
        TrafficStats.getTotalRxBytes();//手机wifi，移动网络下载的总流量
    }
}
