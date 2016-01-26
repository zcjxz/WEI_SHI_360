package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;

import com.zcj.wei_shi_360.R;

public class Setup2 extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

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
