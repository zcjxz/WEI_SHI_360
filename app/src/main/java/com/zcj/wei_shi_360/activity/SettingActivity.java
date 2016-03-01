package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.service.AddressService;
import com.zcj.wei_shi_360.view.SettingItemView;


public class SettingActivity extends AppCompatActivity {

    private SettingItemView sivUpdate;
    private SettingItemView sivAddress;
    private SharedPreferences mPerf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPerf = getSharedPreferences("config", MODE_PRIVATE);
        //初始化自动更新
        initUpdate();
        //初始化归属地显示
        initAddressView();
    }
    private void initUpdate(){
        sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
//        sivUpdate.setTitle("自动更新设置");
        boolean autoUpdate = mPerf.getBoolean("auto update", true);
        if (autoUpdate){
//            sivUpdate.setDesc("自动更新已开启");
            sivUpdate.setCheck(true);
        }else{
//            sivUpdate.setDesc("自动更新已关闭启");
            sivUpdate.setCheck(false);
        }
        sivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivUpdate.isChecked()){
                    sivUpdate.setCheck(false);
//                sivUpdate.setDesc("自动更新已关闭");
                    mPerf.edit().putBoolean("auto update",false).commit();
                }else{
                    sivUpdate.setCheck(true);
//                sivUpdate.setDesc("自动更新已开启");
                    mPerf.edit().putBoolean("auto update", true).commit();
                }
            }
        });
    }
    private void initAddressView(){
        sivAddress = (SettingItemView) findViewById(R.id.siv_address);
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()){
                    sivAddress.setCheck(false);
                    stopService(new Intent(SettingActivity.this,AddressService.class));
                }else{
                    sivAddress.setCheck(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));
                }
            }
        });
    }
}
