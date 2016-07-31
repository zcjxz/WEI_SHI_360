package com.zcj.wei_shi_360.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.utils.SmsUtils;

public class AToolsActivity extends AppCompatActivity {
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }
    public void startAppLock(View view){
        startActivity(new Intent(AToolsActivity.this,AppLock.class));
    }
    public void numberAddressQuery(View view){
        Intent intent=new Intent(this,AddressActivity.class);
        startActivity(intent);
    }
    public void smsBackup(View view){
        pd=new ProgressDialog(AToolsActivity.this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在备份");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    SmsUtils.backupSms(AToolsActivity.this, new SmsUtils.BackUpCallBack() {
                        @Override
                        public void setCount(int max) {
                            pd.setMax(max);
                        }

                        @Override
                        public void setProgress(int progress) {
                            pd.setProgress(progress);
                        }
                    });
                    runOnUiThread(new Thread(){
                        @Override
                        public void run() {
                            Toast.makeText(AToolsActivity.this, "备份成功，路径为sd卡根目录，backup.xml", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("备份出错:",e.toString());
                    runOnUiThread(new Thread(){
                        @Override
                        public void run() {
                            Toast.makeText(AToolsActivity.this, "备份失败！！！", Toast.LENGTH_SHORT).show();

                        }
                    });
                }finally {
                    pd.dismiss();
                }
            }
        }.start();

    }

    public void smsRestore(View view){
        Uri uri=Uri.parse("content://sms/");
        getContentResolver().registerContentObserver(uri,true,new SmsObserver(new Handler()));
        SmsUtils.restoreSms(AToolsActivity.this);
        Toast.makeText(AToolsActivity.this, "安卓4.4的限制，此功能仅做测试", Toast.LENGTH_SHORT).show();
    }
    private class SmsObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            Log.i("SmsObserver","短信数据库发生了变化");
            super.onChange(selfChange);
        }
    }
}
