package com.sikefeng.tongxuelu.utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by sikefeng on 2016/8/14.
 */
public class FileUtils {
    //我写的获取文件大小?
    public static String getFileSize(long filesize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String filesizesString = "";
        if (filesize < 1024) {
            filesizesString = df.format((double) filesize) + "B";
        } else if (filesize < (1024 * 1024)) {
            filesizesString = df.format((double) filesize / 1024) + "K";
        } else if (filesize < (1024 * 1024 * 1024)) {
            filesizesString = df.format((double) filesize / (1024 * 1024)) + "M";
        } else {
            filesizesString = df.format((double) filesize / (1024 * 1024 * 1024)) + "G";
        }
        return filesizesString;
    }

    //我写的获取文件大小?
    public static String getFileSize(File file) {
        long filesize = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        String filesizesString = "";
        if (filesize < 1024) {
            filesizesString = df.format((double) filesize) + "B";
        } else if (filesize < (1024 * 1024)) {
            filesizesString = df.format((double) filesize / 1024) + "K";
        } else if (filesize < (1024 * 1024 * 1024)) {
            filesizesString = df.format((double) filesize / (1024 * 1024)) + "M";
        } else {
            filesizesString = df.format((double) filesize / (1024 * 1024 * 1024)) + "G";
        }
        return filesizesString;
    }

    public static String getFileNameByURL(String url) {
        //获取网络文件名称**********
        if (url.indexOf("?") > -1) {
            String filename1 = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));//获取文件名和扩展名
            return filename1;
        } else {
            String expandname = url.substring(url.lastIndexOf(".") + 1, url.length()).toLowerCase();//获取文件扩展名
//		String filename2=load_path.substring(load_path.lastIndexOf("/")+1,load_path.lastIndexOf("."));//获取文件名
            String filename2 = url.substring(url.lastIndexOf("/") + 1, url.length());//获取文件名
            return filename2;
        }
    }

    //删除文件
    public static boolean deleteFile(String path) {
        File deleteFile = new File(path);
        if (deleteFile.exists()) {
            deleteFile.delete();
            return true;
        }
        return false;
    }
}
