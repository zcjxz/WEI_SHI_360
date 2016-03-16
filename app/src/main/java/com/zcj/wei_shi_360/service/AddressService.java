package com.zcj.wei_shi_360.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.dbUtils.AddressUtils;


public class    AddressService extends Service{

    private TelephonyManager telephonyManager;
    private MyListener listener;
    private OutCallReceiver receiver;
    private WindowManager windowManager;
    private View view;
    private SharedPreferences mPref;
    private int startX;
    private int startY;
    private int winWidth;
    private int winHeight;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//监听电话的状态
        //注册广播接收者
        receiver=new OutCallReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receiver, filter);
        windowManager= (WindowManager) getSystemService(WINDOW_SERVICE);
        winWidth = windowManager.getDefaultDisplay().getWidth();
        winHeight = windowManager.getDefaultDisplay().getHeight();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(listener,PhoneStateListener.LISTEN_NONE);//停止来电监听
        unregisterReceiver(receiver);
        receiver=null;
    }

    private class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d("AAA", "onCallStateChanged: 响铃");
                    String address = AddressUtils.getAddress(incomingNumber);
//                    Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG).show();
                    myToast(address);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("AAA", "onCallStateChanged: 摘机");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("AAA", "onCallStateChanged: 待机");
                    if (view!=null){
                        windowManager.removeView(view);
                    }
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    }
    //服务里面的内部类
    class OutCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //拿到拨出的电话号码
            String phone=getResultData();
            String address = AddressUtils.getAddress(phone);
//            Toast.makeText(context, "address", Toast.LENGTH_LONG).show();
            myToast(address);
        }
    }
    //自定义吐司
    private void myToast(String address) {
        view = View.inflate(this, R.layout.address_show, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_address);
        textView.setText(address);
        int[] bgs=new int[]{
          R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue,
                R.drawable.call_locate_gray,R.drawable.call_locate_green
        };
        int address_style = mPref.getInt("address_style", 0);
        view.setBackgroundResource(bgs[address_style]);
        //窗体参数
        final WindowManager.LayoutParams params=new WindowManager.LayoutParams();
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags=WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format= PixelFormat.TRANSLUCENT;//让吐司半透明
//        params.type= WindowManager.LayoutParams.TYPE_TOAST;//type_toast 的权限较低
        params.type=WindowManager.LayoutParams.TYPE_PHONE;//电话窗口用于电话交互，它置于所有应用之上，需要权限
        params.gravity= Gravity.LEFT+Gravity.TOP;//将中心位置设置为左上方，默认是屏幕中心
        //设置view的位置偏移量
        int lastX = mPref.getInt("lastX", 200);
        int lastY = mPref.getInt("lastY", 250);
        params.x=lastX;
        params.y=lastY;
        windowManager.addView(view,params);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startX = (int)event.getRawX();
                        startY =(int)event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX= (int) event.getRawX();
                        int endY= (int) event.getRawY();
                        int dx=endX-startX;
                        int dy=endY-startY;
                        params.x+=dx;
                        params.y+=dy;
                        //防止坐标偏移出屏幕
                        if (params.x<0){
                            params.x=0;
                        }
                        if (params.y<0){
                            params.y=0;
                        }
                        if (params.x>winWidth-view.getWidth()){
                            params.x=winWidth-view.getWidth();
                        }
                        if (params.y>winHeight-view.getHeight()){
                            params.y=winHeight-view.getHeight();
                        }
                        //更新悬浮窗
                        windowManager.updateViewLayout(view,params);
                        //重新初始化坐标
                        startX= (int) event.getRawX();
                        startY= (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mPref.edit().putInt("lastX", params.x).commit();
                        mPref.edit().putInt("lastY",params.y).commit();
                        break;
                    default:
                }
                return true;
            }
        });
    }
}
