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
    private DevicePolicyManager mDPM;
    private ComponentName mComponentName;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("SmsReceiver", "收到短信");
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
                    mDPM = (DevicePolicyManager) context.getSystemService(context.DEVICE_POLICY_SERVICE);
                    mComponentName = new ComponentName(context, AdminReceiver.class);
                    boolean deviced = config.getBoolean("deviced", false);
                    if (mDPM.isAdminActive(mComponentName)){
                        smsManage.sendTextMessage(safe_phone, null, "成功锁屏", null, null);
                        mDPM.lockNow();
                        mDPM.resetPassword(substring, 0);
                    }else{
                        smsManage.sendTextMessage(safe_phone, null, "未激活设备管理器，该功能无法使用", null, null);
                    }
                }else if ("#*wipedata*#".equals(messageBody)){
                    abortBroadcast();//拦截短信
                    if (mDPM.isAdminActive(mComponentName)){
                        mDPM.wipeData(DevicePolicyManager.WIPE_RESET_PROTECTION_DATA);//清除手机数据
//                        mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//清除外置存储卡
                        smsManage.sendTextMessage(safe_phone, null, "成功清除数据", null, null);
                    }else{
                        smsManage.sendTextMessage(safe_phone, null, "未激活设备管理器，该功能无法使用", null, null);
                    }
                }
            }
        }
    }
}
