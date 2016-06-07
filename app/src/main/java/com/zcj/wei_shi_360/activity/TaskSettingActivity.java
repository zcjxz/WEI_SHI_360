package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.service.AutoCleanService;
import com.zcj.wei_shi_360.utils.ServiceStatusUtils;

public class TaskSettingActivity extends AppCompatActivity {

    private CheckBox show_system;
    private SharedPreferences sp;
    private CheckBox auto_clean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_setting);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        auto_clean = (CheckBox) findViewById(R.id.cb_auto_clean);
        show_system = (CheckBox) findViewById(R.id.cb_show_system);
        show_system.setChecked(sp.getBoolean("showSystem",false));
        show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("showSystem",isChecked);
                edit.commit();
            }
        });
        auto_clean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent=new Intent(TaskSettingActivity.this, AutoCleanService.class);
                if (isChecked){
                    startService(intent);
                }else{
                    stopService(intent);
                }
            }
        });
        boolean serviceRunning = ServiceStatusUtils.isServiceRunning(TaskSettingActivity.this, "com.zcj.wei_shi_360.service.AutoCleanService");
        auto_clean.setChecked(serviceRunning);
//        /**
//         * 定时任务
//         */
//        CountDownTimer timer=new CountDownTimer(3000,1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                System.out.println(millisUntilFinished);
//            }
//
//            @Override
//            public void onFinish() {
//                System.out.println("finish");
//            }
//        };
//        timer.start();
    }
}
