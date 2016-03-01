package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.dbUtils.AddressUtils;

public class AToolsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }
    public void numberAddressQuery(View view){
        Intent intent=new Intent(this,AddressActivity.class);
        startActivity(intent);
    }
}
