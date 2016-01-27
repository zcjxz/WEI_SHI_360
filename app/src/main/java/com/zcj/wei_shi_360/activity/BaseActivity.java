package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;


public abstract class BaseActivity extends Activity {
    private GestureDetector gestureDetector;
    public SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(Math.abs(e2.getRawY()-e1.getRawY())>100){
                    Toast.makeText(BaseActivity.this, "不能这样划哦", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (Math.abs(velocityX)<150){
                    Toast.makeText(BaseActivity.this,"划得太慢了",Toast.LENGTH_SHORT).show();
                }
                if (e1.getRawX() - e2.getRawX() > 200) {
                    showNext();
                }
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPrevious();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        config = getSharedPreferences("config", MODE_PRIVATE);
    }
    public void next(View v) {
        showNext();
    }

    public void previous(View v) {
        showPrevious();
    }
    public abstract void showNext();
    public abstract void showPrevious();
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
