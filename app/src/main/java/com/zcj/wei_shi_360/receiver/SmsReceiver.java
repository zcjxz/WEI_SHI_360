package com.zcj.wei_shi_360.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.zcj.wei_shi_360.R;


public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AAA", "拦截短信");
        SharedPreferences config = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String safe_phone = config.getString("safe_phone", "");
        Object[] objects= (Object[]) intent.getExtras().get("pdus");
        for(Object object:objects){
            SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = message.getOriginatingAddress();
            String messageBody = message.getMessageBody();
            if("aaa".equals(messageBody)&originatingAddress.endsWith(safe_phone)){
                MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                mediaPlayer.setVolume(0.5f,0.5f);//音量
                mediaPlayer.setLooping(true);//循环
                mediaPlayer.start();//开始播放
                abortBroadcast();//拦截短信
                Log.d("ZZZZZZ", "拦截短信");
            }
        }
    }
}
