package com.zcj.wei_shi_360.dbUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 曾灿杰 on 2016/3/23.
 */
public class BlackNumberDao{

    private final BlackNumberOpenHelper helper;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }
    public boolean add(String number,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("number",number);
        contentValues.put("mode", mode);
        long rowId = db.insert("blacknumber", null, contentValues);//row为行id
        if (rowId==-1){
            return false;
        }else{
        return true;
        }
    }
    public boolean delete(String number){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});//rowNumber为影响的行数
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
        int rowNumber = db.update("blacknumber", contentValues, "number=?", new String[]{number});//rowNumber为影响的行数
        if (rowNumber==0){
            return false;
        }else {
            return true;
        }
    }
    public String findNumber(String number){
        String mode="";
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("blacknumber", new String[]{mode}, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()){
            mode=cursor.getString(0);
        }
        cursor.close();
        return mode;
    }

}
