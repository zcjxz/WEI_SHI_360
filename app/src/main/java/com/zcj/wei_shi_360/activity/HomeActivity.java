package com.zcj.wei_shi_360.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.utils.MD5Utils;

public class HomeActivity extends AppCompatActivity {
    private GridView gv_home;
    private String[] mItems=new String[]{
         "手机防盗","通信卫士","软件管理",
         "进程管理","流量统计","手机杀毒",
          "缓存清理","高级工具","设置中心"
    };
    private int[] mPics=new int[]{
      R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
            R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
            R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
    };
    private SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        config = getSharedPreferences("config", MODE_PRIVATE);
        gv_home= (GridView) findViewById(R.id.gv_home);
        gv_home.setAdapter(new Myadapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        //手机防盗
                        showPasswordDialog();
                        break;
                    case 1:
                        //通讯卫士
                        startActivity(new Intent(HomeActivity.this,CallSafeActivity.class));
                        break;
                    case 2:
                        //软件管理
                        startActivity(new Intent(HomeActivity.this,AppManagerActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomeActivity.this,TaskManagerActivity.class));
                        //进程管理
                        break;
                    case 4:
                        //流量统计
                        startActivity(new Intent(HomeActivity.this,TrafficManagerActivity.class));
                        break;
                    case 5:
                        //手机杀毒
                        startActivity(new Intent(HomeActivity.this,AntiVirusActivity.class));
                        break;
                    case 6:
                        //缓存清理
                        break;
                    case 7:
                        //高级设置
                    startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
                        break;case 8:
                        //设置中心
                    startActivity(new Intent(HomeActivity.this,SettingActivity.class));
                        break;

                }
            }
        });
    }

    private void showPasswordDialog() {
        String saved_password = config.getString("password", null);
        if (!TextUtils.isEmpty(saved_password)){
            showPasswordInputDialog();
        }else{
            showPasswordSetDialog();
        }
    }

    private void showPasswordInputDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog=builder.create();
        View view=View.inflate(this,R.layout.dialog_input_password,null);
        dialog.setView(view,0,0,0,0);
        final EditText et_input_password = (EditText) view.findViewById(R.id.et_input_password);
        Button confirm = (Button) view.findViewById(R.id.bt_confirm);
        Button cancel = (Button) view.findViewById(R.id.bt_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String save_password = config.getString("password", null);
                String input_password = et_input_password.getText().toString();
                if (!TextUtils.isEmpty(input_password)){
                    if (MD5Utils.encode(input_password).equals(save_password)){
                        Toast.makeText(HomeActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this,LostFindActivity.class));
                    }else {
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showPasswordSetDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog=builder.create();
        View view=View.inflate(this,R.layout.dialog_set_password,null);
        dialog.setView(view, 0, 0, 0, 0);
        final EditText et_password = (EditText) view.findViewById(R.id.et_password);
        final EditText et_password_confirm = (EditText) view.findViewById(R.id.et_password_confirm);
        Button bt_confirm= (Button) view.findViewById(R.id.bt_confirm);
        Button bt_cancel= (Button) view.findViewById(R.id.bt_cancel);
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = et_password.getText().toString();
                String password_confirm = et_password_confirm.getText().toString();
                if (!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(password_confirm)){
                    if (password.equals(password_confirm)) {
                        config.edit().putString("password", MD5Utils.encode(password)).commit();
                        Toast.makeText(HomeActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                    }else{
                        Toast.makeText(HomeActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(HomeActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    class Myadapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView==null){
                convertView=View.inflate(HomeActivity.this,R.layout.home_item,null);
                holder.iv= (ImageView) convertView.findViewById(R.id.iv_item_home);
                holder.tv= (TextView) convertView.findViewById(R.id.tv_item_home);
                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            holder.iv.setImageResource(mPics[position]);
            holder.tv.setText(mItems[position]);
            return convertView;
        }
    }
    class ViewHolder{
        ImageView iv;
        TextView tv;
    }
}
