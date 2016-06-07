package com.zcj.wei_shi_360.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.bean.AppInfo;
import com.zcj.wei_shi_360.bean.TaskInfo;
import com.zcj.wei_shi_360.engine.TaskInfoProvider;
import com.zcj.wei_shi_360.utils.SystemInfoUntil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class TaskManagerActivity extends AppCompatActivity {

    private TextView tv_process_count;
    private TextView tv_mem_info;
    private LinearLayout ll_loading;
    private ListView listView;
    private List<TaskInfo> allTaskInfos;
    private List<TaskInfo> userTaskInfos;
    private List<TaskInfo> systemTaskInfos;
    private TaskAdapter adapter;
    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    setListData();
                    break;


            }
        }
    };
    private TextView tv_status;

    /**
     * 为listview设置适配器，填充数据
     */
    private void setListData() {
        ll_loading.setVisibility(View.VISIBLE);
        if (adapter==null){
            adapter=new TaskAdapter();
            listView.setAdapter(adapter);
            tv_status.setText("用户进程：");
            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    if (firstVisibleItem<=userTaskInfos.size()){
                        tv_status.setText("用户进程：");
                    }else if(firstVisibleItem>=(userTaskInfos.size()+1)){
                        tv_status.setText("系统进程：");
                    }
                }
            });
        }else{
            adapter.notifyDataSetChanged();
        }
        ll_loading.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        tv_process_count = (TextView) findViewById(R.id.tv_process_count);
        tv_mem_info = (TextView) findViewById(R.id.tv_mem_info);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        listView = (ListView) findViewById(R.id.lv_task_manager);
        tv_status = (TextView) findViewById(R.id.tv_status);
        fillData();
    }

    private void setTitle() {
        int runingProcessCount = SystemInfoUntil.getRuningProcessCount(this);
        tv_process_count.setText("运行中的进程"+"("+runingProcessCount+")");
        long availMem = SystemInfoUntil.getAvailMem(this);
        long totalMem = SystemInfoUntil.getTotalMem(this);
        tv_mem_info.setText("可用："+Formatter.formatFileSize(this,availMem)+"/"+Formatter.formatFileSize(this,totalMem));

    }

    /**
     * 获取所有运行的进程的信息
     */
    private void fillData() {
        ll_loading.setVisibility(View.VISIBLE);
        new Thread(){
            @Override
            public void run() {
                allTaskInfos=TaskInfoProvider.getTaskInfo(TaskManagerActivity.this);
                userTaskInfos=new ArrayList<TaskInfo>();
                systemTaskInfos=new ArrayList<TaskInfo>();
                for (TaskInfo info:allTaskInfos){
                    if (info.isUserTask()){
                        userTaskInfos.add(info);
                    }else{
                        systemTaskInfos.add(info);
                    }
                }
                //发送信息，更新界面
                Message msg = handler.obtainMessage();
                msg.what=1;
                handler.sendMessage(msg);

            }
        }.start();
        setTitle();
    }

    /**
     * 适配器
     */
    class TaskAdapter extends BaseAdapter{
        @Override
        public int getItemViewType(int position) {
            int result;
            if (position==0||position==(userTaskInfos.size()+1)){
                result=1;//标签
            }else if (position<=userTaskInfos.size()){
                result=2;//用户进程
            }else{
                result=3;//系统进程
            }
            return result;
        }

        @Override
        public int getViewTypeCount() {
            return 4;
        }

        @Override
        public int getCount() {
            SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
            boolean showSystem = sp.getBoolean("showSystem", false);
            if (showSystem) {
                return userTaskInfos.size() + systemTaskInfos.size() + 2;
            }else{
                return userTaskInfos.size()+1;
            }
        }

        @Override
        public Object getItem(int position) {
            TaskInfo info;
            if (getItemViewType(position)==1){
                return null;
            }else{
                if (position<=userTaskInfos.size()){
                    info=userTaskInfos.get(position-1);
                }else{
                    info=systemTaskInfos.get(position-2);
                }
                return info;
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int type=getItemViewType(position);
            ViewHolder viewHolder;
            if (convertView==null){
                viewHolder=new ViewHolder();
                if (type==1){
                    convertView = View.inflate(TaskManagerActivity.this, R.layout.lv_app_info_dao_hang, null);
                    viewHolder.tag = (TextView) convertView.findViewById(R.id.lv_app_info_dao_hang);
                    convertView.setTag(viewHolder);
                }else{
                    convertView=View.inflate(TaskManagerActivity.this,R.layout.list_item_task_manager,null);
                    viewHolder.icon= (ImageView) convertView.findViewById(R.id.iv_taskIcon);
                    viewHolder.name= (TextView) convertView.findViewById(R.id.tv_taskName);
                    viewHolder.memsize= (TextView) convertView.findViewById(R.id.tv_memSize);
                    viewHolder.checkBox= (CheckBox) convertView.findViewById(R.id.cb_check);
                    convertView.setTag(viewHolder);
                }
                Log.i("convertView","新创建了"+position);
            }else{
                viewHolder= (ViewHolder) convertView.getTag();
                Log.i("convertView","复用了"+position);
            }

            if (type==1){
                if (position==0){
                    viewHolder.tag.setText("用户进程");
                }else{
                    viewHolder.tag.setText("系统进程");
                }
            }else{
                viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("checked","改变了");
                        if (((CheckBox) v).isChecked()){
//                            ((CheckBox) v).setChecked(false);
                            if (type==2){
                                userTaskInfos.get(position-1).setChecked(true);
                            }else if (type==3){
                                systemTaskInfos.get(position-2-userTaskInfos.size()).setChecked(true);
                            }
                        }else{
//                            ((CheckBox) v).setChecked(true);
                            if (type==2){
                                userTaskInfos.get(position-1).setChecked(false);
                            }else if (type==3){
                                systemTaskInfos.get(position-2-userTaskInfos.size()).setChecked(false);
                            }
                        }
                        Log.i("user:",""+userTaskInfos.size());
                        Log.i("system:",""+systemTaskInfos.size());
                        Log.i("position:",""+position);
                    }
                });
                if (type==2){
                    int newPosition=position-1;
                    viewHolder.checkBox.setChecked(userTaskInfos.get(newPosition).isChecked());
                    Log.i("isChecked",""+userTaskInfos.get(newPosition).isChecked());
                    viewHolder.icon.setImageDrawable(userTaskInfos.get(newPosition).getIco());
                    viewHolder.name.setText(userTaskInfos.get(newPosition).getName());
                    viewHolder.memsize.setText(Formatter.formatFileSize(TaskManagerActivity.this,userTaskInfos.get(newPosition).getMemsize()));
                    if (getPackageName().equals(userTaskInfos.get(newPosition).getPackname())){
                        viewHolder.checkBox.setVisibility(View.INVISIBLE);
                    }else{
                        viewHolder.checkBox.setVisibility(View.VISIBLE);
                    }
                }else if (type==3){
                    int newPosition=position-userTaskInfos.size()-2;
                    viewHolder.checkBox.setChecked(systemTaskInfos.get(newPosition).isChecked());
                    Log.i("isChecked",""+systemTaskInfos.get(newPosition).isChecked());
                    viewHolder.icon.setImageDrawable(systemTaskInfos.get(newPosition).getIco());
                    viewHolder.name.setText(systemTaskInfos.get(newPosition).getName());
                    viewHolder.memsize.setText(Formatter.formatFileSize(TaskManagerActivity.this,systemTaskInfos.get(newPosition).getMemsize()));
                }


            }
            return convertView;
        }

        class ViewHolder{
            ImageView icon;
            TextView name;
            TextView memsize;
            TextView tag;
            CheckBox checkBox;
        }
    }

    public void selectAll(View view){
        for (TaskInfo info:allTaskInfos){
            info.setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }
    public void selectOpp(View view){
        for (TaskInfo info:allTaskInfos){
            info.setChecked(!info.isChecked());
        }
        adapter.notifyDataSetChanged();
    }
    public void kill(View view){
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (TaskInfo info:allTaskInfos){
            if (info.isChecked()){
                am.killBackgroundProcesses(info.getPackname());
            }
        }
        fillData();
    }
    public void setting(View view){
        startActivityForResult(new Intent(TaskManagerActivity.this,TaskSettingActivity.class),0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
