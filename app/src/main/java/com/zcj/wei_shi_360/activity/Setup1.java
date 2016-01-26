package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcj.wei_shi_360.R;

public class Setup1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);

    }

    public void next(View view) {
        startActivity(new Intent(Setup1.this, Setup2.class));
        finish();
        overridePendingTransition(R.anim.tran_in,R.anim.tran_out);
    }
}
