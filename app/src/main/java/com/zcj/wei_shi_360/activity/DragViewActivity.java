package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;

public class DragViewActivity extends Activity {
    private TextView tv_top;
    private TextView tv_bottom;
    private ImageView iv_darg;
    private int startX;
    private int startY;
    private SharedPreferences mPref;
    private RelativeLayout.LayoutParams layoutParams;
    private long[] mHits=new long[2];//双击的次数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_view);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        tv_top = (TextView) findViewById(R.id.tv_top);
        tv_bottom= (TextView) findViewById(R.id.tv_bottom);
        iv_darg= (ImageView) findViewById(R.id.iv_drag);
        int lastX = mPref.getInt("lastX", 0);
        int lastY = mPref.getInt("lastY", 0);
//        android的底层绘图步骤：onMeasure(测量view),onLayout(安放位置),onDraw(绘图)
//        iv_darg.layout(lastX,lastY,lastX+iv_darg.getWidth(),lastY+iv_darg.getHeight());不能用此方法，因为还没测量完
//                                                                                            不能安放位置
        //获取屏幕宽高
        final int winWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int winHeight = getWindowManager().getDefaultDisplay().getHeight();
        final int statusBarHeight= getStatusBarHeight();
        if (lastY>winWidth/2){
            tv_top.setVisibility(View.VISIBLE);
            tv_bottom.setVisibility(View.INVISIBLE);
        }else{
            tv_top.setVisibility(View.INVISIBLE);
            tv_bottom.setVisibility(View.VISIBLE);
        }
        //获取布局文件
        layoutParams = (RelativeLayout.LayoutParams) iv_darg.getLayoutParams();
        layoutParams.leftMargin=lastX;//设置左边距
        layoutParams.topMargin=lastY;//设置上边距
        iv_darg.setLayoutParams(layoutParams);//重新设置位置

        iv_darg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();
                        int dx = endX - startX;
                        int dy = endY - startY;
                        int l = iv_darg.getLeft() + dx;
                        int r = iv_darg.getRight() + dx;
                        int t = iv_darg.getTop() + dy;
                        int b = iv_darg.getBottom() + dy;
//                        如果提示框超出范围则不更新界面
//                        if (l<0||r>winWidth||t<0||b>winHeiggt-20){
//                            break;
//                        }
//                        //更新界面
//                        iv_darg.layout(l,t,r,b);
                        //更新界面
                        if (l < 0) {
                            layoutParams.leftMargin = 0;
                            layoutParams.topMargin = t;
//                            iv_darg.setLayoutParams(layoutParams);
                        } else if (r > winWidth) {
                            layoutParams.rightMargin = 0;
                            layoutParams.topMargin = t;
//                            iv_darg.setLayoutParams(layoutParams);
                        } else if (t < 0) {
                            layoutParams.topMargin = 0;
                            layoutParams.leftMargin = l;
//                            iv_darg.setLayoutParams(layoutParams);
                        } else if (b > (winHeight - statusBarHeight)) {
                            layoutParams.bottomMargin = 0;
                            layoutParams.leftMargin = l;
//                            iv_darg.setLayoutParams(layoutParams);
                        } else {
                            layoutParams.topMargin = t;
                            layoutParams.leftMargin = l;
//                            iv_darg.setLayoutParams(layoutParams);
                        }
                        iv_darg.setLayoutParams(layoutParams);
                        //控制上下悬浮窗的显示与隐藏
                        if (t > winWidth / 2) {
                            tv_top.setVisibility(View.VISIBLE);
                            tv_bottom.setVisibility(View.INVISIBLE);
                        } else {
                            tv_top.setVisibility(View.INVISIBLE);
                            tv_bottom.setVisibility(View.VISIBLE);
                        }
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        mPref.edit().putInt("lastX", iv_darg.getLeft()).commit();
                        mPref.edit().putInt("lastY", iv_darg.getTop()).commit();
                        break;
                }
                return false;//事件向下传递，响应onclick事件
            }
        });
        iv_darg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits,1,mHits,0,mHits.length-1);
                mHits[mHits.length-1]= SystemClock.uptimeMillis();//开机后计算的时间
                if (mHits[0]>=(SystemClock.uptimeMillis()-500)){
                iv_darg.layout((winWidth-iv_darg.getWidth())/2,(winHeight-iv_darg.getHeight())/2,(winWidth+iv_darg.getWidth())/2,(winHeight+iv_darg.getHeight())/2);

                }
            }
        });
    }
    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
