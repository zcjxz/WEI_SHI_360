package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcj.wei_shi_360.R;

public class Setup4 extends AppCompatActivity {

    private SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        config = getSharedPreferences("config", MODE_PRIVATE);
    }

    public void next(View v) {
        startActivity(new Intent(Setup4.this, LostFindActivity.class));
        finish();
        config.edit().putBoolean("configed",true).commit();
    }
    public void previous(View v){
        startActivity(new Intent(Setup4.this,setup3.class));
        finish();
    }
}
