package com.zcj.wei_shi_360.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by ZCJ on 2016/5/27.
 */
public class TaskInfo {
    private Drawable ico;
    private String name;
    private String packname;
    private long memsize;

    /**
     * 判断是否为用户进程

     */
    private boolean userTask;

    @Override
    public String toString() {
        return "TaskInfo{" +
                "ico=" + ico +
                ", name='" + name + '\'' +
                ", packname='" + packname + '\'' +
                ", memsize=" + memsize +
                ", userTask=" + userTask +
                '}';
    }

    public boolean isUserTask() {
        return userTask;
    }

    public void setUserTask(boolean userTask) {
        this.userTask = userTask;
    }

    public long getMemsize() {
        return memsize;
    }

    public void setMemsize(long memsize) {
        this.memsize = memsize;
    }

    public String getPackname() {
        return packname;
    }

    public void setPackname(String packname) {
        this.packname = packname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIco() {
        return ico;
    }

    public void setIco(Drawable ico) {
        this.ico = ico;
    }

}
