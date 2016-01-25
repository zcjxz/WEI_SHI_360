package com.zcj.wei_shi_360.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 曾灿杰 on 2016/1/24.
 */
public class MD5Utils {
    public static String encode(String password)  {
        MessageDigest digest= null;//获取md5加密对象
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes=digest.digest(password.getBytes());//对字符串加密，返回字节数组
        StringBuffer buffer=new StringBuffer();
        for (byte b:bytes){
            int i=b & 0xff;//获取低八位有效值
            String hexString = Integer.toHexString(i);
            if (hexString.length()<2){
                hexString="0"+hexString;//如果是一位，补零
            }
            buffer.append(hexString);
        }
        return buffer.toString();
    }
}
