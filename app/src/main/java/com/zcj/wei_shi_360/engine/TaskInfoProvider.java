package com.zcj.wei_shi_360.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Debug;

import com.zcj.wei_shi_360.bean.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**提供手机里的进程信息
 *
 */
public class TaskInfoProvider {
    /**
     * 获取所有的进程信息
     * @param context 上下文
     * @return
     */
    public static List<TaskInfo> getTaskInfo(Context context){
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> ProcessesInfo = am.getRunningAppProcesses();
        List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();
        for (ActivityManager.RunningAppProcessInfo processInfo:ProcessesInfo){
            TaskInfo taskInfo=new TaskInfo();
            //获取应用的包名
            String packName = processInfo.processName;
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{processInfo.pid});
            long memSize = processMemoryInfo[0].getTotalPrivateDirty() * 1024;
            try {
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packName, 0);
                Drawable ic = applicationInfo.loadIcon(pm);
                String name=applicationInfo.loadLabel(pm).toString();
                boolean isUserTask;
                if ((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0){
                    isUserTask=false;
                }else{
                    isUserTask=true;
                }
                taskInfo.setPackname(packName);
                taskInfo.setIco(ic);
                taskInfo.setName(name);
                taskInfo.setUserTask(isUserTask);
                taskInfo.setMemsize(memSize);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            taskInfos.add(taskInfo);
        }

        return taskInfos;
    }
}
