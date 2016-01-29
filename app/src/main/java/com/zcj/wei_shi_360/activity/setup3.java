package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;

import org.w3c.dom.Text;

public class setup3 extends BaseActivity {


    private EditText et_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        et_phone = (EditText) findViewById(R.id.et_phone);
        String safe_phone = config.getString("safe_phone", "");
        et_phone.setText(safe_phone);
    }

    @Override
    public void showNext() {
        String phone = et_phone.getText().toString().trim();//过滤空格
        if (TextUtils.isEmpty(phone)){
            Toast.makeText(setup3.this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            config.edit().putString("safe_phone",phone).commit();
            startActivity(new Intent(setup3.this, Setup4.class));
            finish();
            overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
        }
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
        if (resultCode== Activity.RESULT_OK){
        String phone = data.getStringExtra("phone");
        et_phone.setText(phone);
        }
    }
}
