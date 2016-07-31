package com.zcj.wei_shi_360.dbUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirsDao {
    public static boolean isVirius(String md5){
        boolean result=false;
        String path="data/data/com.zcj.wei_shi_360/files/antivirus.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select * from datable where md5 = ?", new String[]{md5});
        if (cursor.moveToNext()){
            result=true;
        }
        db.close();
        return result;
    }
}
