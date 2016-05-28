package com.zcj.wei_shi_360.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ZCJ on 2016/5/20.
 */
public class SmsUtils {
    /**
     * 备份短信的回掉接口
     */
    public interface BackUpCallBack{
        /**
         * 设置备份短信进度的总进度
         * @param max 备份短信的总进度的值
         */
        void setCount(int max);

        /**
         * 设置备份短信的当前进度
         * @param progress 备份短信的当前进度的值
         */
        void setProgress(int progress);
    }

    private static File file;

    /**
     * 短信备份
     * @param context
     * @param callBack
     * @throws Exception
     */
    public static void backupSms(Context context,BackUpCallBack callBack)throws Exception{
        ContentResolver resolver=context.getContentResolver();
        file = new File(Environment.getExternalStorageDirectory(),"backup.xml");
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos=new FileOutputStream(file);
        XmlSerializer serializer= Xml.newSerializer();
        serializer.setOutput(fos,"utf-8");
        serializer.startDocument("utf-8",true);
        serializer.startTag(null,"smss");
        Uri uri=Uri.parse("content://sms/");
        Cursor cursor = resolver.query(uri, new String[]{
                "body", "address", "type", "date"
        }, null, null, null);
        int max=cursor.getCount();
        int progress=0;
        serializer.attribute(null,"max",max+"");
        callBack.setCount(max);
        while(cursor.moveToNext()){
            serializer.startTag(null,"sms");

            String body=cursor.getString(0);
            String address=cursor.getString(1);
            String type=cursor.getString(2);
            String date=cursor.getString(3);

            serializer.startTag(null,"body");
            serializer.text(body);
            serializer.endTag(null,"body");

            serializer.startTag(null,"address");
            serializer.text(address);
            serializer.endTag(null,"address");

            serializer.startTag(null,"type");
            serializer.text(type);
            serializer.endTag(null,"type");

            serializer.startTag(null,"date");
            serializer.text(date);
            serializer.endTag(null,"date");

            serializer.endTag(null,"sms");

            progress++;
            callBack.setProgress(progress);
        }

        serializer.endTag(null,"smss");
        serializer.endDocument();
        fos.close();
    }

    /**恢复短信
     *
     */
    public static void restoreSms(Context context){
        Uri uri=Uri.parse("content://sms/");
        ContentValues values=new ContentValues();
        values.put("body","有人举报你装逼，请跟我们走一趟。");
        values.put("date",System.currentTimeMillis()+"");
        values.put("type","1");
        values.put("address","110");
//        values.put("read","0");
//        values.put("person","110");
        ContentResolver resolver = context.getContentResolver();
        Uri insertUri=resolver.insert(uri,values);
        Log.i("inertUri",insertUri+"");
    }
}
