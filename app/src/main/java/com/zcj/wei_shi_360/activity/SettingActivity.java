package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.dbUtils.AddressUtils;
import com.zcj.wei_shi_360.service.AddressService;
import com.zcj.wei_shi_360.utils.ServiceStatusUtils;
import com.zcj.wei_shi_360.view.SettingClickItemView;
import com.zcj.wei_shi_360.view.SettingItemView;


public class SettingActivity extends AppCompatActivity {

    private SettingItemView sivUpdate;
    private SettingItemView sivAddress;
    private SharedPreferences mPerf;
    private SettingClickItemView sivAddressStyle;
    final String[] items=new String[]{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mPerf = getSharedPreferences("config", MODE_PRIVATE);
        //初始化自动更新
        initUpdate();
        //初始化归属地显示
        initAddressView();
        //初始化归属地显示框风格
        initAddressStyle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.zcj.wei_shi_360.service.AddressService");
        if (serviceRunning){
            sivAddress.setCheck(true);
        }else{
            sivAddress.setCheck(false);
        }
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
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(this, "com.zcj.wei_shi_360.service.AddressService");
        if (serviceRunning){
            sivAddress.setCheck(true);
        }else{
            sivAddress.setCheck(false);
        }
        sivAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAddress.isChecked()) {
                    sivAddress.setCheck(false);
                    stopService(new Intent(SettingActivity.this, AddressService.class));
                } else {
                    sivAddress.setCheck(true);
                    startService(new Intent(SettingActivity.this, AddressService.class));
                }
            }
        });

    }
    private void initAddressStyle(){
        String chosed_style=null;
        sivAddressStyle = (SettingClickItemView) findViewById(R.id.siv_address_style);
        sivAddressStyle.setTitle("归属地提示框风格");
        sivAddressStyle.setTitle("活力橙");
        int address_style = mPerf.getInt("address_style", 0);
        sivAddressStyle.setDesc(items[address_style]);
        sivAddressStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleChooseDailog();
            }
        });
    }

    private void showSingleChooseDailog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("选择归属地提示框风格");
        int address_style = mPerf.getInt("address_style", 0);
        builder.setSingleChoiceItems(items,address_style,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
            mPerf.edit().putInt("address_style",which).commit();
            dialog.dismiss();
            sivAddressStyle.setDesc(items[which]);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
