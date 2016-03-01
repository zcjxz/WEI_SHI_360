package com.zcj.wei_shi_360.activity;

import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.dbUtils.AddressUtils;

public class AddressActivity extends AppCompatActivity {

    private EditText phone;
    private TextView tv_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        phone = (EditText) findViewById(R.id.et_address_phone);
        tv_address = (TextView) findViewById(R.id.tv_show_address_phone);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = AddressUtils.getAddress(s.toString());
                tv_address.setText(address);
                //抖动效果
//                Animation shake= AnimationUtils.loadAnimation(AddressActivity.this,R.anim.shake);
//                phone.startAnimation(shake);
                //震动
                vibrate();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void vibrate(){
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
//        vibrator.vibrate(2000); 震动两秒
        vibrator.vibrate(new long[]{
        1000,1000,1500,500
        },-1);//先等一秒，震一秒，等1.5秒，震0.5秒
                //-1 表示不循环。其他整数表示从哪一位开始循环
    }
}
