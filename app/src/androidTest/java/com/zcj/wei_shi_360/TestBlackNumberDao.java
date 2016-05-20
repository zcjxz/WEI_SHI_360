package com.zcj.wei_shi_360;

import android.content.Context;
import android.test.AndroidTestCase;

import com.zcj.wei_shi_360.dbUtils.BlackNumberDao;

import java.util.Random;


/**
 * Created by 曾灿杰 on 2016/4/25.
 */
public class TestBlackNumberDao extends AndroidTestCase {
    public Context context;

    @Override
    protected void setUp() throws Exception {
        this.context=getContext();
        super.setUp();
    }
    public void test(){
        BlackNumberDao dao=new BlackNumberDao(context);
        Random random=new Random();
        for (int i = 0; i < 100; i++) {
            String number="";
            dao.add(number+i,String.valueOf(random.nextInt(3)+1));
        }
    }
}
