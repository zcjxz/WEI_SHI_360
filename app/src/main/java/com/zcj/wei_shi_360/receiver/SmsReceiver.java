package com.zcj.wei_shi_360.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.activity.AdminReceiver;
import com.zcj.wei_shi_360.service.LocationService;


public class SmsReceiver extends BroadcastReceiver {

    private String safe_phone;
    private SmsManager smsManage;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AAA", "拦截短信");
        SharedPreferences config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        safe_phone = config.getString("safe_phone", "");
        Object[] objects= (Object[]) intent.getExtras().get("pdus");
        smsManage = SmsManager.getDefault();
        for(Object object:objects) {
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = message.getOriginatingAddress();
            String messageBody = message.getMessageBody();
            if (originatingAddress.endsWith(safe_phone)) {

                if ("#*alarm*#".equals(messageBody)) {
                    abortBroadcast();//拦截短信
                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setVolume(0.5f, 0.5f);//音量
                    mediaPlayer.setLooping(true);//循环
                    mediaPlayer.start();//开始播放
                    Log.d("ZZZ", "拦截短信");
                } else if ("#*location*#".equals(messageBody)) {
                    abortBroadcast();//拦截短信
                    context.startService(new Intent(context, LocationService.class));//开启定位服务
                    String longitude = config.getString("longitude", "还未获取到位置");
                    String latitude = config.getString("latitude", "还未获取到位置");
                    smsManage.sendTextMessage(safe_phone, null, "经度：" + longitude + ";纬度：" + latitude, null, null);
                }else if (messageBody.startsWith("#*lockscreen*#")){
                    abortBroadcast();//拦截短信
                    String substring = messageBody.substring("#*lockscreen*#".length(), messageBody.length());
                    //获取设备策略服务
                    DevicePolicyManager mDPM = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
                    boolean deviced = config.getBoolean("deviced", false);
                    if (deviced){
                        mDPM.lockNow();
                        mDPM.resetPassword(substring, 0);
                        smsManage.sendTextMessage(safe_phone, null, "成功锁屏", null, null);
                    }else{
                        smsManage.sendTextMessage(safe_phone, null, "未激活设备管理器，该功能无法使用", null, null);
                    }
                }else if ("#*wipedata*#".equals(messageBody)){
                    abortBroadcast();//拦截短信

                }
            }
        }
    }
}
