package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zcj.wei_shi_360.R;

public class Setup4 extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        final CheckBox cb_protect = (CheckBox) findViewById(R.id.cb_protect);
        boolean protect = config.getBoolean("protect", false);
        if (protect) {
            cb_protect.setText("防盗保护已经开启");
            cb_protect.setChecked(true);
        }else{
            cb_protect.setText("防盗保护已经关闭");
            cb_protect.setChecked(false);
        }

        cb_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_protect.setText("防盗保护已经开启");
                    config.edit().putBoolean("protect",true).commit();
                }else{
                    cb_protect.setText("防盗保护已经关闭");
                    config.edit().putBoolean("protect",false).commit();
                }
            }
        });
    }

    @Override
    public void showNext() {
        startActivity(new Intent(Setup4.this, LostFindActivity.class));
        finish();
        config.edit().putBoolean("configed",true).commit();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(Setup4.this,setup3.class));
        finish();
        overridePendingTransition(R.anim.tran_up, R.anim.tran_down);
    }
}
