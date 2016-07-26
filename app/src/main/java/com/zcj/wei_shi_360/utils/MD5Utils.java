package com.zcj.wei_shi_360.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Utils {
    /**
     * 对文件进行加密
     * @param password
     * @return
     */
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

    /**
     * 获取文件的MD5值
     * @param sourceDir
     * @return
     */
    public static String getFileMD5(String sourceDir){
        StringBuffer sb=new StringBuffer();
        File file = new File(sourceDir);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len=-1;
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            while((len=fis.read(buffer))!=-1){
                messageDigest.update(buffer,0,len);
            }
            byte[] result = messageDigest.digest();
            for (byte b:result){
                int i=b & 0xff;//获取低八位有效值
                String hexString = Integer.toHexString(i);
                if (hexString.length()==1){
                    sb.append("0"+hexString);//如果是一位，补零
                }else {
                    sb.append(hexString);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
