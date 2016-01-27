package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcj.wei_shi_360.R;

public class Setup4 extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

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
