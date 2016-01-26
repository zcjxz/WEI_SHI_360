package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.zcj.wei_shi_360.R;


public abstract class BaseActivity extends Activity {
    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (e1.getRawX() - e2.getRawX() > 200) {
                    showNext();
                }
                if (e2.getRawX() - e1.getRawX() > 200) {
                    showPrevious();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
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
