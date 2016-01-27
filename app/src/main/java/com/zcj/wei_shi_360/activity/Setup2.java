package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.view.SettingItemView;

public class Setup2 extends BaseActivity {


    private SettingItemView siv_sim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);
        siv_sim = (SettingItemView) findViewById(R.id.siv_sim);
        String sim = config.getString("sim", null);
        if (!TextUtils.isEmpty(sim)){
            siv_sim.setCheck(true);
        }else{
            siv_sim.setCheck(false);
        }
        siv_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(siv_sim.isChecked()){
                    siv_sim.setCheck(false);
                    config.edit().remove("sim").commit();
                }else{
                    siv_sim.setCheck(true);
                    TelephonyManager tm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber();//获取sim卡序列号
                    config.edit().putString("sim",simSerialNumber).commit();
                }
            }
        });
    }

    @Override
    public void showNext() {
        startActivity(new Intent(Setup2.this, setup3.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(Setup2.this, Setup1.class));
        finish();
        overridePendingTransition(R.anim.tran_up, R.anim.tran_down);
    }



}
