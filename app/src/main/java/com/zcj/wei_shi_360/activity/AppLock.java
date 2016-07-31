package com.zcj.wei_shi_360.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.fragment.LockFragment;
import com.zcj.wei_shi_360.fragment.UnLockFragment;

import org.w3c.dom.Text;

public class AppLock extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private LockFragment lockFragment;
    private UnLockFragment unLockFragment;
    private TextView tv_unlock;
    private TextView tv_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        tv_lock = (TextView) findViewById(R.id.tv_lock);
        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        //获取manager
        fragmentManager = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //创建fragment
        lockFragment = new LockFragment();
        unLockFragment = new UnLockFragment();
        //切换fragment
        transaction.replace(R.id.tv_show_is_lock,lockFragment).commit();
        tv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.tv_show_is_lock,lockFragment).commit();

            }
        });
        tv_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.tv_show_is_lock,unLockFragment).commit();

            }
        });
    }
}
