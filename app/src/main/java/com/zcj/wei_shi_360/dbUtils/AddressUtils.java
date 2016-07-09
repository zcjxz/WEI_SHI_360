package com.zcj.wei_shi_360.dbUtils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressUtils {
    private static final String PATH = "data/data/com.zcj.wei_shi_360/files/address.db";

    public static String getAddress(String number) {
        String address = "未知号码";
        SQLiteDatabase database = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
        if (number.matches("^1[3-8]\\d{9}$")) {
            Cursor cursor = database.rawQuery("select location from data2 where id=(select outkey from data1 where id = ?)", new String[]{number.substring(0, 7)});
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                address = cursor.getString(0);
                cursor.close();
            }
        } else if (number.matches("^\\d+$")) {
            switch (number.length()) {
                case 3:
                    if (number.equals("110")) {
                        address = "报警电话";
                    } else if (number.equals("120")) {
                        address = "急救电话";
                    } else if (number.equals("119")) {
                        address = "火警电话";
                    }
                    break;
                case 5:
                    address = "客服电话";
                    break;
                case 7:
                case 8:
                    address = "本地电话";
                default:
                    if (number.startsWith("0") && number.length() > 10) {
                        Cursor cursor = database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 4)});
                        if (cursor.getCount() != 0) {
                            cursor.moveToFirst();
                            address = cursor.getString(0);
                        } else {
                            cursor.close();
                            cursor = database.rawQuery("select location from data2 where area = ?", new String[]{number.substring(1, 3)});
                            if (cursor.getCount() != 0) {
                                cursor.moveToFirst();
                                address = cursor.getString(0);
                            }
                        }
                    }
                    break;
            }

        }
        database.close();
        return address;
    }
}
