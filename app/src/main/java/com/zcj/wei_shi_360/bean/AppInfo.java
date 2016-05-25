package com.zcj.wei_shi_360.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by ZCJ on 2016/5/22.
 */
public class AppInfo {
    private Drawable icon;
    private String name;
    private String packName;
    private boolean inRom;
    private boolean userApp;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }
}
