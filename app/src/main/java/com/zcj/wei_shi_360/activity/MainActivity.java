package com.zcj.wei_shi_360.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;


import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zcj.wei_shi_360.R;
import com.zcj.wei_shi_360.utils.StreamUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.logging.LogRecord;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {
    private static final int CODE_UPDATE_DIALOG = 0;
    private static final int CODE_JSON_ERROR = 1;
    private static final int CODE_URL_ERROR = 2;
    private static final int CODE_NET_ERROR = 3;
    private static final int CODE_ENTER_HOME = 4;
    TextView version;
    TextView tv_progress;
    private String mVersionName;
    private int mVersionCode;
    private String mDesc;
    private String mDownloadUrl;
    private SharedPreferences mPerf;
    private Handler handler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
        switch (msg.what){
            case CODE_JSON_ERROR:
                Toast.makeText(MainActivity.this, "join解析错误", Toast.LENGTH_SHORT).show();
                enter();
                break;
            case CODE_NET_ERROR:
                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                enter();
                break;
            case CODE_UPDATE_DIALOG:
                showUpdateDailog();
                break;
            case CODE_URL_ERROR:
                Toast.makeText(MainActivity.this, "url错误", Toast.LENGTH_SHORT).show();
                enter();
                break;
            case CODE_ENTER_HOME:

                enter();
                break;

        }
        }
    };
    private RelativeLayout rl_root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        tv_progress= (TextView) findViewById(R.id.tv_progress);
        version= (TextView) findViewById(R.id.tv_version);
        rl_root = (RelativeLayout) findViewById(R.id.rl_root);
        version.setText("版本号：" + getmVersionName());
        mPerf = getSharedPreferences("config", MODE_PRIVATE);
        boolean autoUpdate = mPerf.getBoolean("auto update", true);
        if (autoUpdate) {
            checkVersion();
        }else{
            //发送延迟两秒的消息
            handler.sendEmptyMessageDelayed(CODE_ENTER_HOME,2000);
        }
        AlphaAnimation animation=new AlphaAnimation(0.3f,1f);
        animation.setDuration(2000);
        rl_root.startAnimation(animation);
    }



    private String getmVersionName(){
        PackageManager manager=getPackageManager();
        try {
            PackageInfo info=manager.getPackageInfo(getPackageName(),0);
            String versionName=info.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "出错";
    }
    private int getmVersionCode(){
        PackageManager manager=getPackageManager();
        try {
            PackageInfo info=manager.getPackageInfo(getPackageName(),0);

            int versionCode=info.versionCode;
            return versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
    private void checkVersion(  ){
        new Thread(){
            private HttpURLConnection connection;
            Message message=Message.obtain();
            @Override
            public void run() {
                long startTime=System.currentTimeMillis();
                try {
                    URL url=new URL("http://192.168.1.103/document/update.json");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    connection.connect();

                    int responseCode= connection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is= connection.getInputStream();
                        String result=StreamUtils.readFromStream(is);
                        System.out.println("网络返回："+result);
                        try {
                            JSONObject jo=new JSONObject(result);
                            mVersionName = jo.getString("versionName");
                            mVersionCode = jo.getInt("versionCode");
                            mDesc=jo.getString("description");
                            mDownloadUrl=jo.getString("downloadUrl");
                            System.out.println("版本描述："+mDesc);

                            if (mVersionCode>getmVersionCode()){
//                                showUpdateDailog();
                                message.what=CODE_UPDATE_DIALOG;
                            }else {
                                message.what=CODE_ENTER_HOME;
                            }
                        } catch (JSONException e) {
                            //json解析错误
                            message.what=CODE_JSON_ERROR;
                            e.printStackTrace();

                        }

                    }
                } catch (MalformedURLException e) {
                    //url错误
                    message.what=CODE_URL_ERROR;
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误
                    message.what=CODE_NET_ERROR;
                    e.printStackTrace();
                }
                finally {

                    long endTime=System.currentTimeMillis();
                    long useTime=endTime-startTime;
                    if (useTime<2000){
                        try {
                            Thread.sleep(2000-useTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    handler.sendMessage(message);
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }.start();

    }
    private void showUpdateDailog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("最新版本" + mVersionCode);
        builder.setMessage(mDesc);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.print("立即更新");
                download();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("以后再说");
                enter();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enter();
            }
        });
        builder.create().show();
    }
    private void enter(){
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
    protected void download(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            String target= Environment.getExternalStorageDirectory()+"/update.apk";
            HttpUtils utils=new HttpUtils();
            utils.download(mDownloadUrl, target, new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                System.out.println("下载进度："+current+"/"+total);
                    System.out.println("是否下载："+isUploading);
                    tv_progress.setVisibility(View.VISIBLE);
                    tv_progress.setText("下载进度："+current*100/total+"%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                System.out.println("下载成功");
                    Intent intent=new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result),
                            "application/vnd.android.package-archive");
                    startActivityForResult(intent, 0);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    System.out.println("下载失败");
                }
            });
        }else {
            Toast.makeText(MainActivity.this, "没有找到sd卡", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enter();
    }
}
