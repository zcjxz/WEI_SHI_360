package com.zcj.wei_shi_360.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.zcj.wei_shi_360.R;

public class CallSafeActivity extends AppCompatActivity {

    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        initUI();
    }

    private void initUI() {
        list_view = (ListView) findViewById(R.id.list_view);
    }
}
