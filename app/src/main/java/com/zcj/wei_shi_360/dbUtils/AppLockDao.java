package com.zcj.wei_shi_360.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZCJ on 2016/6/3.
 */
public class AppLockDao {
    static String TABLE_NAME="AppLock";
    static String ROW_PACK_NAME="packName";
    AppLockOpenHelper helper;
    private SQLiteDatabase db;
    private Context context;
    private final Intent changeIntent;

    public AppLockDao(Context context) {
        helper=new AppLockOpenHelper(context);
        this.context=context;
        changeIntent = new Intent();
        changeIntent.setAction("com.zcj.mobileSafe.appLockChange");
    }

    /**
     * 添加一条数据
     * @param packName
     * @return 返回影响的行数，如果出错返回“-1”
     */
    public long add(String packName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("packName",packName);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        context.sendBroadcast(changeIntent);
        return result;
    }

    /**
     * 删除一条数据
     * @param packName
     * @return  返回删除的行数，如果返回“0”则删除失败
     */
    public int delete(String packName){
        SQLiteDatabase db = helper.getWritableDatabase();
        int delete = db.delete(TABLE_NAME, ROW_PACK_NAME + "=?", new String[]{packName});
        db.close();
        context.sendBroadcast(changeIntent);
        return delete;
    }

    /**
     * 查询数据是否在数据库里
     * @param packName
     * @return  true：存在， false：不存在
     */
    public boolean find(String packName){
        boolean result=false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor query = db.query(TABLE_NAME, null, ROW_PACK_NAME + "=?", new String[]{packName}, null, null, null);
        if(query.moveToNext()){
            result=true;
        }
        db.close();
        return result;
    }

    /**
     * 查询所有包名
     * @return  返回所有包名的列表
     */
    public List<String> findAll(){
        List<String> protectPackName=new ArrayList<String>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"packname"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            protectPackName.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return protectPackName;
    }
}
