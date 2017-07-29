package com.sikefeng.tongxuelu.utils;

/**
 * Created by sikefeng on 2016/9/21.
 */

import android.app.Activity;
import android.content.Intent;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 调用系统相机拍照工具类
 * @author yao
 *
 */
public class CaremaUtil {
    private static String strImgPath = "";// 照片的绝对路径
    private static String imgPath = "";// 照片所在文件夹路径
    // 保存的拍照文件
    private static File out;
    /**
     * 相机照相并返回图片名字
     */
    public static String cameraMethod(Activity activity, int requestCode) {
        // 实例化拍照的Intent
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 设置图片存放的路径，Environment.getExternalStorageDirectory()得到SD卡的根目录
        // 先验证手机是否有sdcard
        String status = Environment.getExternalStorageState();
        if ((Environment.MEDIA_MOUNTED).equals(status)) {
            strImgPath = getImgPath();
            // 本地保存原图
            File file = new File(strImgPath);
            Uri u = Uri.fromFile(file);
            imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, u);
            // 启动ACITIVITY
            activity.startActivityForResult(imageCaptureIntent, requestCode);
        } else {
            Toast.makeText(activity, "没有储存卡", Toast.LENGTH_SHORT).show();
        }
        return getImageName();
    }
    /**
     * 返回图片的绝对路径
     *
     * @return
     */
    public static String getImgPath() {
        imgPath = getImagePath();// 存放照片的文件夹
//        LogUtil.log("图片存放的路径-----" + strImgPath);
        String imageName = getImageName();
//        LogUtil.log("图片全名路径---" + imageName);
        // 检查存放的路径是否存在，如果不存在则创建目录
        try {
            out = new File(imgPath);
            if (!out.exists()) {
                out.mkdirs();
            }
            // 在此目录下创建文件
            out = new File(imgPath, imageName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 该照片的绝对路径
        strImgPath = imgPath + imageName;
        return strImgPath;
    }
    /**
     * 得到图片所在的文件夹的路径
     *
     * @return
     */
    public static String getImagePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/";//kkkkkkkkkkkkkkkkkkkkkkkkkkk
    }
    /**
     * 得到图片的名字
     *
     * @return
     */
    public static String getImageName() {
        // 给相片命名
        String imageName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";// 照片命名
        return imageName;
    }

    /**
     * 清除本地拍照缓存
     */
    public static void clearCacheImage() {
        File filecache = new File(getImagePath());
        if (filecache != null && filecache.listFiles() != null) {
            for (File file : filecache.listFiles()) {
                if (file.isFile()) {
                    file.delete(); // 删除所有文件
                }
            }
        }
    }

    /**
     * 获取原始图片的角度（解决三星手机拍照后图片是横着的问题）
     * @param path 图片的绝对路径
     * @return 原始图片的角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}