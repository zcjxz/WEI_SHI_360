package com.zcj.wei_shi_360;

import android.test.AndroidTestCase;
import android.util.Log;

import com.zcj.wei_shi_360.bean.TaskInfo;
import com.zcj.wei_shi_360.engine.TaskInfoProvider;

import java.util.List;

/**
 * Created by ZCJ on 2016/5/28.
 */
public class TestTaskInfoProvider extends AndroidTestCase{
    public void testGetTestInfo() throws Exception{
        List<TaskInfo> taskInfos = TaskInfoProvider.getTaskInfo(getContext());
        for (TaskInfo info:taskInfos){
            System.out.println(info.toString());
        }
    }
}
