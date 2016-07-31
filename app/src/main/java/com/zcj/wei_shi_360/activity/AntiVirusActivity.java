package com.zcj.wei_shi_360.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.dbUtils.AntivirsDao;
import com.zcj.wei_shi_360.utils.MD5Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class AntiVirusActivity extends AppCompatActivity {

    private static final int SCANING = 0;
    private static final int SCAN_BEFORE = -1;
    private static final int SCAN_FINISH = 1;
    private ImageView iv_scan;
    private ProgressBar progressBar;
    private PackageManager pm;
    private TextView tv_scan_status;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SCAN_BEFORE:
                    progressBar.setMax((Integer) msg.obj);
                    break;
                case SCANING:
                    ScanInfo scanInfo= (ScanInfo) msg.obj;
                    tv_scan_status.setText(scanInfo.appName);
                    progressBar.setProgress(progressBar.getProgress()+1);
                    TextView tv=new TextView(AntiVirusActivity.this);
                    if (scanInfo.isVirus){
                        tv.setTextColor(Color.RED);
                        tv.setText("发现病毒："+scanInfo.appName);
                    }else{

                        tv.setText("扫描安全："+scanInfo.appName);
                    }
                    ll_scan.addView(tv,0);
                    break;
                case SCAN_FINISH:
                    tv_scan_status.setText("");
                    tv_isFinish.setText("扫描完成！");
                    iv_scan.clearAnimation();
            }
        }
    };
    private LinearLayout ll_scan;
    private TextView tv_isFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        pm = getPackageManager();
        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
        ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
        tv_isFinish = (TextView) findViewById(R.id.tv_scan_isFinish);
        RotateAnimation ra=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(1000);
        ra.setRepeatCount(Animation.INFINITE);//不断重复
        iv_scan.startAnimation(ra);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        scanVirus();
    }
    private void scanVirus(){
        tv_scan_status.setText("正在初始化十六核杀毒引擎。。。。");
        new Thread(){
            @Override
            public void run() {
                List<PackageInfo> infos = pm.getInstalledPackages(0);
                Message msg=Message.obtain();
                msg.what=SCAN_BEFORE;
                msg.obj=infos.size();
                handler.sendMessage(msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (PackageInfo info:infos){
                    ScanInfo scanInfo=new ScanInfo();
                    scanInfo.packName=info.packageName;
                    scanInfo.appName=info.applicationInfo.loadLabel(pm).toString();
                    //数据路径
//                    String dataDir = info.applicationInfo.dataDir;
                    //获取到应用的安装apk文件
                    String sourceDir = info.applicationInfo.sourceDir;
                    //获取应用的md5值
                    String md5 = MD5Utils.getFileMD5(sourceDir);
                    //判断应用的md5值是否在病毒数据库中
                    if(AntivirsDao.isVirius(md5)){
                        //发现病毒
                        scanInfo.isVirus=true;
                    }else{
                        //未发现病毒
                        scanInfo.isVirus=false;
                    }
                    msg=Message.obtain();
                    msg.obj=scanInfo;
                    msg.what=SCANING;
                    handler.sendMessage(msg);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                msg=Message.obtain();
                msg.what=SCAN_FINISH;
                handler.sendMessage(msg);
            }
        }.start();

    }

    /**
     * 扫描信息的内部类
     */
    class ScanInfo{
        String packName;
        String appName;
        boolean isVirus;
    }
}
