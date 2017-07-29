package com.sikefeng.tongxuelu.version;

import cn.bmob.v3.BmobObject;

/**
 * Created by sikefeng on 2016/8/11.
 */
public class AppMsg extends BmobObject {

    private int appVersion;
    private String appUrl;

    public AppMsg() {

    }

    public AppMsg(int appVersion, String appUrl) {
        this.appVersion = appVersion;
        this.appUrl = appUrl;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }
}
