package com.zcj.wei_shi_360.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zcj.wei_shi_360.R;

public class AntiVirusActivity extends AppCompatActivity {

    private ImageView iv_scan;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_virus);
        iv_scan = (ImageView) findViewById(R.id.iv_scan);
        RotateAnimation ra=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        ra.setDuration(1000);
        ra.setRepeatCount(Animation.INFINITE);//不断重复
        iv_scan.startAnimation(ra);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(100);
        new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < 101; i++) {
                    try {
                        sleep(100);
                        progressBar.setProgress(i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
