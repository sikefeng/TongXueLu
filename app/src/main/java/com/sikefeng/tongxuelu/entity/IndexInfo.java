package com.sikefeng.tongxuelu.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by sikefeng on 2016/9/16.
 */
public class IndexInfo extends BmobObject {

    private String imgUrl;
    private String content;
    private String website;
    private String title;
    private boolean isChecked;
    public IndexInfo(){

    }

    public IndexInfo(String imgUrl, String content) {
        this.imgUrl = imgUrl;
        this.content = content;
    }

    public IndexInfo(String content,String title,String imgUrl,  String website) {
        this.imgUrl = imgUrl;
        this.content = content;
        this.title = title;
        this.website = website;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
