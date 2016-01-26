package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcj.wei_shi_360.R;

public class Setup2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

    }
    public void next(View v){
        startActivity(new Intent(Setup2.this,setup3.class));
        finish();
    }
    public void previous(View v){
        startActivity(new Intent(Setup2.this,Setup1.class));
        finish();
    }
}
