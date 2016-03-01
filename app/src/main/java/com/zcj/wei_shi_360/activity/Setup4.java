package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;

public class Setup4 extends BaseActivity {


    private ComponentName mComponentName;
    private DevicePolicyManager mDPM;
    private CheckBox device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);
        final CheckBox cb_protect = (CheckBox) findViewById(R.id.cb_protect);
        device = (CheckBox) findViewById(R.id.deviced);
        boolean protect = config.getBoolean("protect", false);
        //获取设备策略服务
        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(Setup4.this, AdminReceiver.class);
        //判断是否激活设备管理器
        if (mDPM.isAdminActive(mComponentName)){
            device.setChecked(true);
        }else{
            device.setChecked(false);
        }
        if (protect) {
            cb_protect.setText("防盗保护已经开启");
            cb_protect.setChecked(true);
        }else{
            cb_protect.setText("防盗保护已经关闭");
            cb_protect.setChecked(false);
        }
        device.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,R.string.description);
                    startActivityForResult(intent, 0);//在回掉函数中判断是否激活
                } else {
                    if (mDPM.isAdminActive(mComponentName)){
                    mDPM.removeActiveAdmin(mComponentName);//取消激活
                    config.edit().putBoolean("decived", false).commit();
                    Toast.makeText(Setup4.this, "取消激活成功！！！", Toast.LENGTH_SHORT).show();}
                }
            }
        });
        cb_protect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cb_protect.setText("防盗保护已经开启");
                    config.edit().putBoolean("protect",true).commit();
                }else{
                    cb_protect.setText("防盗保护已经关闭");
                    config.edit().putBoolean("protect",false).commit();
                }
            }
        });
    }

    @Override
    public void showNext() {
        startActivity(new Intent(Setup4.this, LostFindActivity.class));
        finish();
        config.edit().putBoolean("configed",true).commit();
        overridePendingTransition(R.anim.tran_in, R.anim.tran_out);
    }

    @Override
    public void showPrevious() {
        startActivity(new Intent(Setup4.this,setup3.class));
        finish();
        overridePendingTransition(R.anim.tran_up, R.anim.tran_down);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== Activity.RESULT_OK){
            if (mDPM.isAdminActive(mComponentName)){
                Toast.makeText(Setup4.this, "激活成功！！！", Toast.LENGTH_SHORT).show();
                config.edit().putBoolean("deviced",true).commit();
            }else{
                device.setChecked(false);
                Toast.makeText(Setup4.this, "激活失败，远程锁屏和清除数据将无法使用！！！aaaa", Toast.LENGTH_SHORT).show();
            }
        }else{
            device.setChecked(false);
            Toast.makeText(Setup4.this, "激活失败，远程锁屏和清除数据将无法使用！！！", Toast.LENGTH_SHORT).show();
        }
    }
}
