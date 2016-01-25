package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.zcj.wei_shi_360.R;

/**
 * Created by 曾灿杰 on 2016/1/24.
 */
public class LostFindActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = config.getBoolean("configed", false);
        if (configed){
            setContentView(R.layout.activity_lost_find);
        }else{
            startActivity(new Intent(LostFindActivity.this,Setup1.class));
            finish();
        }
    }
}
