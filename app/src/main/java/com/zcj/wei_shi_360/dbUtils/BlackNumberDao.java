package com.zcj.wei_shi_360.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

import com.zcj.wei_shi_360.bean.BlackNumberInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 曾灿杰 on 2016/3/23.
 */
public class BlackNumberDao{

    private final BlackNumberOpenHelper helper;
    private final String BlackNumber="blacknumber";

    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }


    public boolean add(String number,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("number",number);
        contentValues.put("mode", mode);
        long rowId = db.insert(BlackNumber, null, contentValues);//row为行id
        db.close();
        if (rowId==-1){
            return false;
        }else{
             return true;
        }
    }
    public boolean delete(String number){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete(BlackNumber, "number=?", new String[]{number});//rowNumber为影响的行数
        db.close();
        if (rowNumber==0){
            return false;
        }else {
            return true;
        }

    }
    public boolean changeNumberMode(String number,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mode",mode);
        int rowNumber = db.update(BlackNumber, contentValues, "number=?", new String[]{number});//rowNumber为影响的行数
        db.close();
        if (rowNumber==0){
            return false;
        }else {
            return true;
        }
    }
    public String findNumber(String number){
        String mode=null;
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(BlackNumber, new String[]{"mode"}, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()){
            mode=cursor.getString(0);
        }
        cursor.close();
        db.close();
        return mode;
    }
    public List<BlackNumberInfo> findAll(){
        ArrayList<BlackNumberInfo> blackNumberInfos=new ArrayList<BlackNumberInfo>();
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query(BlackNumber, new String[]{"number", "mode"}, null, null, null, null, null);
        while(cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        SystemClock.sleep(3000);
        return blackNumberInfos;
    }
    //分页加载
    //limit 限制只获取多少条数据
    //offset 跳过多少条数据
    public List<BlackNumberInfo> findPar(int PageNumber,int PageSize){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{
                String.valueOf(PageSize), String.valueOf(PageSize * (PageNumber-1))
        });
        ArrayList<BlackNumberInfo> blackNumberInfos=new ArrayList<BlackNumberInfo>();
        while(cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }
    //分批加载
    public List<BlackNumberInfo> findPi(int StartIndex,int maxCount){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{
                String.valueOf(maxCount), String.valueOf(StartIndex)
        });
        ArrayList<BlackNumberInfo> blackNumberInfos=new ArrayList<BlackNumberInfo>();
        while(cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo=new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();
        return blackNumberInfos;
    }
    //获取总的记录条数
    public int getTotalNumber(){
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}
