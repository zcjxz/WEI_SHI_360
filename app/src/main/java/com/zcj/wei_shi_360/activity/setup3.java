package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zcj.wei_shi_360.R;

public class setup3 extends BaseActivity {


    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_phone = (EditText) findViewById(R.id.et_phone);
    }

    @Override
    public void showNext() {
        startActivity(new Intent(setup3.this,Setup4.class));
        finish();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(setup3.this,Setup2.class));
        finish();
        overridePendingTransition(R.anim.tran_up, R.anim.tran_down);
    }
    public void selectContact(View view){
        Intent intent = new Intent(setup3.this, ContactActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phone = data.getStringExtra("phone");
        et_phone.setText(phone);
    }
}
