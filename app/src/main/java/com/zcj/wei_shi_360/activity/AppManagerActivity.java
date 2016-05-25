package com.zcj.wei_shi_360.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.Formatter;

import android.view.Gravity;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.bean.AppInfo;
import com.zcj.wei_shi_360.engine.AppInfoProvider;
import com.zcj.wei_shi_360.utils.DensityUtil;


import java.util.ArrayList;
import java.util.List;


public class AppManagerActivity extends AppCompatActivity {

    private static final int LOADED_APPINFOS=0;
    private TextView tv_avail_rom;
    private TextView tv_avail_sd;
    private ListView listView;
    private LinearLayout loading;
    private List<AppInfo> appInfos;
    private List<AppInfo> userAppInfos;
    private List<AppInfo> systemAppInfos;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOADED_APPINFOS:
                    setListViewData();
                    setListener();
                    loading.setVisibility(View.INVISIBLE);
                    break;

            }
        }
    };
    private TextView tv_status;
    private PopupWindow popupWindow;
    private LinearLayout ll_start;
    private LinearLayout ll_uninstall;
    private LinearLayout ll_share;
    private AppInfoManagerAdapter appInfoManagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
        tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
        listView = (ListView) findViewById(R.id.lv_app_manager);
        loading = (LinearLayout) findViewById(R.id.ll_loading);
        tv_status = (TextView) findViewById(R.id.tv_status);

        long romSize=getAvailSpace(Environment.getDataDirectory().getAbsolutePath());
        long sdSize=getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
        tv_avail_rom.setText("内存可用："+ Formatter.formatFileSize(this,romSize));
        tv_avail_sd.setText("SD卡可用："+ Formatter.formatFileSize(this,sdSize));

        loading.setVisibility(View.VISIBLE);
        appInfos = new ArrayList<AppInfo>();
        fillData();

    }

    /**
     * 填充数据
     */
    private void fillData(){
        new Thread(){
            @Override
            public void run() {
                appInfos = AppInfoProvider.getAppInfos(AppManagerActivity.this);
                userAppInfos=new ArrayList<AppInfo>();
                systemAppInfos=new ArrayList<AppInfo>();
                for (AppInfo info:appInfos){
                    if (info.isUserApp()){
                        userAppInfos.add(info);
                    }else{
                        systemAppInfos.add(info);
                    }
                }
                Message msg = handler.obtainMessage();
                msg.what=LOADED_APPINFOS;
                handler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 获取某个路径的可用空间
     * @param path 要获取的路径
     * @return 返回可用的空间的大小
     */
    private long getAvailSpace(String path){
        StatFs statFs=new StatFs(path);
        statFs.getBlockCount();//获取分区的个数
        long size = statFs.getBlockSize();//获取分区的大小
        long availableCount = statFs.getAvailableBlocks();//获取可用的分区的个数
        return size*availableCount;
    }




    /**
     * 得到数据后，绘制界面
     */
    private void setListViewData(){
        if (appInfoManagerAdapter==null){
        appInfoManagerAdapter = new AppInfoManagerAdapter();
        listView.setAdapter(appInfoManagerAdapter);
        }else{
            appInfoManagerAdapter.notifyDataSetChanged();
        }

    }

    private void setListener(){
        //设置滑动的监听
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                dismissPopupWindow();
                if (firstVisibleItem<=userAppInfos.size()){
                    tv_status.setText("用户程序：");
                }else{
                    tv_status.setText("系统程序：");
                }
            }
        });
        //设置item的监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            AppInfo appInfo;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position==0||position==userAppInfos.size()+1){
//
//                }else if(position<=userAppInfos.size()){//用户程序
//                    int newPosition=position-1;
//                    appInfo=userAppInfos.get(newPosition);
//                }else{                                  //系统程序
//                    int newPosition=position-1-userAppInfos.size()-1;
//                    appInfo=systemAppInfos.get(newPosition);
//                }
                Object object=listView.getItemAtPosition(position);
                if (object!=null&&object instanceof AppInfo){
//                    if(position<=userAppInfos.size()){//用户程序
//                        int newPosition=position-1;
//                        appInfo=userAppInfos.get(newPosition);
//                    }else{                                  //系统程序
//                        int newPosition=position-1-userAppInfos.size()-1;
//                        appInfo=systemAppInfos.get(newPosition);
//                    }
                    appInfo= (AppInfo) object;
                }
                dismissPopupWindow();
                RelativeLayout layout=new RelativeLayout(AppManagerActivity.this);
                View contentView=View.inflate(AppManagerActivity.this,R.layout.popup_window,null);
                ll_start =  (LinearLayout) contentView.findViewById(R.id.ll_start);
                ll_uninstall =  (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
                ll_share =  (LinearLayout) contentView.findViewById(R.id.ll_share);
                ll_start.setOnClickListener(new popupOnClick(appInfo));
                ll_uninstall.setOnClickListener(new popupOnClick(appInfo));
                ll_share.setOnClickListener(new popupOnClick(appInfo));
                layout.addView(contentView);
                ViewGroup.LayoutParams layoutParams=contentView.getLayoutParams();
                layoutParams.height=view.getHeight();
                contentView.setLayoutParams(layoutParams);
                contentView.invalidate();
                int[] view_location=new int[2];
                view.getLocationInWindow(view_location);//获取当前item的view的位置
                popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                //动画播放的效果必须要有背景颜色
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置透明的背景颜色
                int left_dp=65;
                int left_px = DensityUtil.dip2px(AppManagerActivity.this, left_dp);
                popupWindow.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,left_px,view_location[1]);
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(300);
                AlphaAnimation alphaAnimation=new AlphaAnimation(0,1.0f);
                alphaAnimation.setDuration(300);
                AnimationSet set=new AnimationSet(false);
                set.addAnimation(scaleAnimation);
                set.addAnimation(alphaAnimation);
                contentView.startAnimation(set);
            }
        });
    }
    private void dismissPopupWindow(){
        if (popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
            popupWindow=null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissPopupWindow();
        super.onDestroy();
    }


    /**
     * 启动应用
     */
    private void startApplication(AppInfo appInfo) {
        PackageManager pm=getPackageManager();
//        Intent intent=new Intent();
//        intent.setAction("android.intent.action.MAIN");
//        intent.addCategory("android.intent.category.LAUNCHER");
//        //查询出所有能启动的Activity
//        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.GET_INTENT_FILTERS);
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackName());
        if (intent!=null){
            startActivity(intent);
        }else{
            Toast.makeText(AppManagerActivity.this, "该应用不能启动", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 卸载应用
     */
    private void uninstallApplication(AppInfo appinfo){
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:"+appinfo.getPackName()));
        startActivity(intent);
    }

    /**
     * 类
     */


    private class AppInfoManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userAppInfos.size()+1+systemAppInfos.size()+1;
        }

        @Override
        public Object getItem(int position) {

            if (position==0||position==userAppInfos.size()+1){
                return null;
            }

            AppInfo appInfo;

            if (position < userAppInfos.size() + 1) {

                appInfo = userAppInfos.get(position - 1);

            } else {

                int location = userAppInfos.size() + 2;

                appInfo = systemAppInfos.get(position - location);
            }

            return appInfo;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            int result;
            if (position==0||position==userAppInfos.size()+1){
                result=1;
            }else{
                result=0;
            }
            return result;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AppInfo appInfo;
            ViewHolder viewHolder;
            int isTag = getItemViewType(position);
            if (convertView==null) {
                if (isTag == 1) {
                    convertView = View.inflate(AppManagerActivity.this, R.layout.lv_app_info_dao_hang, null);
                    viewHolder=new ViewHolder();
                    viewHolder.tv_Tag = (TextView) convertView.findViewById(R.id.lv_app_info_dao_hang);
                    convertView.setTag(viewHolder);
                }else{
                    convertView=View.inflate(AppManagerActivity.this,R.layout.list_item_appinfo,null);
                    viewHolder=new ViewHolder();
                    viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_appName);
                    viewHolder.tv_location= (TextView) convertView.findViewById(R.id.tv_appLocation);
                    viewHolder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_appIcon);
                    convertView.setTag(viewHolder);
                }
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
            }


            if (isTag==1){
                if (position==0){
                    viewHolder.tv_Tag.setText("用户程序：");
                }else{
                    viewHolder.tv_Tag.setText("系统程序：");
                }
            }else{
                if (position<=userAppInfos.size()){
                    int newPosition=position-1;
                    appInfo=userAppInfos.get(newPosition);
                }else{
                    int newPosition=position-1-userAppInfos.size()-1;
                    appInfo=systemAppInfos.get(newPosition);
                }

                viewHolder.iv_icon.setImageDrawable(appInfo.getIcon());
                viewHolder.tv_name.setText(appInfo.getName());
                if(appInfo.isInRom()){
                    viewHolder.tv_location.setText("手机内存");
                }else{
                    viewHolder.tv_location.setText("外部存储");
                }
            }

            return convertView;
        }
    }
    private class ViewHolder{
        TextView tv_Tag;
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_location;
    }
    class popupOnClick implements View.OnClickListener {
        private AppInfo appinfo;
        public popupOnClick(AppInfo appinfo){
            this.appinfo=appinfo;
        }
        @Override
        public void onClick(View v) {
            dismissPopupWindow();
            switch (v.getId()){
                case R.id.ll_start:
                    startApplication(appinfo);
                    break;
                case R.id.ll_share:

                    break;
                case R.id.ll_uninstall:
                    uninstallApplication(appinfo);
                    break;
            }
        }
    }
}
