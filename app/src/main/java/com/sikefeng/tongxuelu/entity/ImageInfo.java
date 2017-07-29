package com.sikefeng.tongxuelu.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by sikefeng on 2016/9/16.
 */
public class ImageInfo extends BmobObject {

    public String  imageUrl;
    public String title;
    public String  website;
    public int id;

    public ImageInfo(String image, String text,String  website) {
        this.imageUrl = image;
        this.title = text;
        this.website = website;
    }

    public ImageInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    @Override
    public String toString() {
        return "ImageInfo{" +
                "imageUrl='" + imageUrl + '\'' +
                ", title='" + title + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
