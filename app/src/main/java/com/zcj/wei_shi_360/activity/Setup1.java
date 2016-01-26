package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcj.wei_shi_360.R;

public class Setup1 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    @Override
    public void showNext() {
        startActivity(new Intent(Setup1.this, Setup2.class));
        finish();
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }

    @Override
    public void showPrevious() {

    }
}
