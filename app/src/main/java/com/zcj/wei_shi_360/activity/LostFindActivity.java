package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;

public class LostFindActivity extends Activity {

    private TextView tv_safePhone;
    private ImageView iv_protect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        boolean configed = config.getBoolean("configed", false);
        if (configed){
            setContentView(R.layout.activity_lost_find);
            tv_safePhone = (TextView) findViewById(R.id.tv_safePhone);
            iv_protect = (ImageView) findViewById(R.id.iv_protect);
            String safePhone = config.getString("safe_phone", "");
            boolean isProtect = config.getBoolean("protect",false);
            tv_safePhone.setText(safePhone);
            if (isProtect){
                iv_protect.setImageResource(R.drawable.lock);
            }else{
                iv_protect.setImageResource(R.drawable.unlock);
            }
        }else{
            startActivity(new Intent(LostFindActivity.this,Setup1.class));
            finish();
        }
    }
    public void reEnterSetrp(View v ){
        startActivity(new Intent(LostFindActivity.this,Setup1.class));
        finish();
    }
}
