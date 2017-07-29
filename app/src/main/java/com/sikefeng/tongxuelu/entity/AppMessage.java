package com.sikefeng.tongxuelu.entity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by Administrator on 2016/6/22.
 */
public class AppMessage {
        private Drawable appIcon;
        private String appName;
        private long appSize;


    private static AppMessage appMessage;

    public static AppMessage getAppMessage(String filePath, Context mcontext) {
        if (AppMessage.appMessage==null){
            AppMessage.appMessage=new AppMessage();
        }
        File file=new File(filePath);
        PackageManager pm=mcontext.getPackageManager();
        PackageInfo pkgInfo=pm.getPackageArchiveInfo(filePath,PackageManager.GET_ACTIVITIES);
        if (pkgInfo!=null){
            ApplicationInfo appInfo=pkgInfo.applicationInfo;
            appInfo.sourceDir=filePath;
            appInfo.publicSourceDir=filePath;
            String appName=pm.getApplicationLabel(appInfo).toString();//应用名称
            Drawable icon1=pm.getApplicationIcon(appInfo);//应用图标
            Drawable icon2=appInfo.loadIcon(pm);//应用图标
            appMessage.setAppName(appName);
            appMessage.setAppIcon(icon1);
            appMessage.setAppSize(file.length());
        }
        return appMessage;
    }
    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
