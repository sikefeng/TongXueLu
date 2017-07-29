package com.sikefeng.tongxuelu.diray;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/24.
 */
public class Book implements Serializable {
    private int id;
    private String title;
    private String content;
    private String saveDate;
    private String textColor;
    private Bitmap background;
    private String textSize;
    private String textStyle;
    public Book() {
        //必须要添加无参构造方法，否则报错
    }
    public Book(String title, String content, String saveDate) {
        this.title = title;
        this.content = content;
        this.saveDate = saveDate;
    }

    public Book(int id,String title, String content, String saveDate) {
        this.id=id;
        this.title = title;
        this.content = content;
        this.saveDate = saveDate;
    }

    public Book(String title, String content, String saveDate, String textColor, Bitmap background, String textSize, String textStyle) {
        this.title = title;
        this.content = content;
        this.saveDate = saveDate;
        this.textColor = textColor;
        this.background = background;
        this.textSize = textSize;
        this.textStyle = textStyle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public String getTextSize() {
        return textSize;
    }

    public void setTextSize(String textSize) {
        this.textSize = textSize;
    }

    public String getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", saveDate='" + saveDate + '\'' +
                ", textColor='" + textColor + '\'' +
                ", background=" + background +
                ", textSize='" + textSize + '\'' +
                ", textStyle='" + textStyle + '\'' +
                '}';
    }
}
