package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;

public class EnterPasswordActivity extends AppCompatActivity {

    private EditText et_password;
    private String packName;
    private Intent intent;
    private TextView tv_name;
    private ImageView iv_icon;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pm = getPackageManager();
        setContentView(R.layout.activity_enter_password);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        intent = getIntent();
        packName = intent.getStringExtra("packName");
        try {
            ApplicationInfo info = pm.getApplicationInfo(packName, 0);
            tv_name.setText("正在保护："+info.loadLabel(pm));
            iv_icon.setImageDrawable(info.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void confirm(View view){
        String password = et_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(EnterPasswordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else{
            if("123".equals(password)){
                //临时停止保护
                Intent intent=new Intent();
                intent.setAction("com.zcj.wei_shi_360.tempStop");
                intent.putExtra("packName",packName);
                sendBroadcast(intent);
                finish();
            }else{
                Toast.makeText(EnterPasswordActivity.this, "密码错误！！！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
