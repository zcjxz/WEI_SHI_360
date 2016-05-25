package com.zcj.wei_shi_360.activity;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.bean.AppInfo;
import com.zcj.wei_shi_360.engine.AppInfoProvider;



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
                    loading.setVisibility(View.INVISIBLE);
                    break;

            }
        }
    };
    private TextView tv_status;
    private PopupWindow popupWindow;



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
        tv_avail_rom.setText("内存可用空间："+ Formatter.formatFileSize(this,romSize));
        tv_avail_sd.setText("SD卡可用空间："+ Formatter.formatFileSize(this,sdSize));

        loading.setVisibility(View.VISIBLE);
        appInfos = new ArrayList<AppInfo>();
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

    private class AppInfoManagerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userAppInfos.size()+1+systemAppInfos.size()+1;
        }

        @Override
        public Object getItem(int position) {
            return position;
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
    private void setListViewData(){
        listView.setAdapter(new AppInfoManagerAdapter());
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            AppInfo appInfo;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0||position==userAppInfos.size()+1){

                }else if(position<=userAppInfos.size()){//用户程序
                    int newPosition=position-1;
                    appInfo=userAppInfos.get(newPosition);
                }else{                                  //系统程序
                    int newPosition=position-1-userAppInfos.size()-1;
                    appInfo=systemAppInfos.get(newPosition);
                }
                dismissPopupWindow();
                TextView textView=new TextView(AppManagerActivity.this);
                textView.setText(appInfo.getName());
                textView.setBackgroundResource(R.color.colorAccent);
                int[] view_location=new int[2];
                view.getLocationInWindow(view_location);
                popupWindow = new PopupWindow(textView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.showAtLocation(parent, Gravity.LEFT|Gravity.TOP,view_location[0],view_location[1]);
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
}
