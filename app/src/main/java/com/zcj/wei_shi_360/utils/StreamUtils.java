package com.zcj.wei_shi_360.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

    private static String result;

    public  static String readFromStream(InputStream is){
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        int len=0;
        byte[] buffer=new byte[1024];
        try {
            while((len=is.read(buffer))!=-1){
                outputStream.write(buffer,0,len);
            }
            result = outputStream.toString();
            is.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return result;
    }
}
