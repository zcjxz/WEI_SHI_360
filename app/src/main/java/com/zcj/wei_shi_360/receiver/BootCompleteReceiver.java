package com.zcj.wei_shi_360.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

//监听手机开机启动的广播
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String sim = config.getString("sim", null);//获取绑定的sim卡序列号
        if (TextUtils.isEmpty(sim)){

        }else{
            //获取手机当前的sim卡序列号
            TelephonyManager tm= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = tm.getSimSerialNumber();//拿到当前手机的sim卡序列号
            if (simSerialNumber.equals(sim)){
                //手机安全
            }else{

            }
        }
    }
}
