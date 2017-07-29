package com.sikefeng.tongxuelu.bmobutils;

import android.app.Activity;
import android.content.Intent;

import com.sikefeng.tongxuelu.activity.CloseActivityClass;
import com.sikefeng.tongxuelu.activity.user.LoginActivity;
import com.sikefeng.tongxuelu.diray.DataImpl;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;

import cn.bmob.v3.BmobUser;

/**
 * Created by sikefeng on 2016/8/12.
 */
public class BmobUtils {


    public static void exitLoginUser(Activity activity){
        BmobUser.logOut(activity);//退出登录
        new DataPersonImpl(activity).deletePersonAll();
        new DataImpl(activity).deleteBookAll();
        activity.startActivity(new Intent(activity,LoginActivity.class));
        CloseActivityClass.closeActivitys(activity);
        activity.finish();
    }

    public static String showErrorMsg(int code){
        switch (code){
            case 9001:
                return "AppKey未初始化";
            case 9002:
                return "解析返回数据出错";
            case 9003:
                return "上传文件出错";
            case 9004:
                return "文件上传失败";
            case 9005:
                return "批量操作只支持最多50条";
            case 9006:
                return "objectId为空";
            case 9007:
                return "文件大小超过10M";
            case 9008:
                return "上传文件不存在";
            case 9009:
                return "没有缓存数据";
            case 9010:
                return "网络连接超时";
            case 9011:
                return "BmobUser类不支持批量操作";
            case 9012:
                return "上下文为空";
            case 9013:
                return "格式不正确";
            case 9014:
                return "第三方账号授权失败";
            case 9015:
                return "其他错误均返回此code";
            case 9016:
                return "网络未连接";
            case 9017:
                return "AppKey";
            case 9018:
                return "参数不能为空";
            case 9019:
                return "格式不正确";
            case 101:
            return "用户名或密码不正确";
            case 102:
            return "格式不正确";
            case 140:
            return "名称已经存在";
            case 145:
            return "文件大小错误";
            case 146:
            return "文件名错误";
            case 149:
            return "空文件";
            case 150:
            return "文件上传错误";
            case 151:
            return "文件删除错误";
            case 160:
            return "图片错误";
            case 161:
            return "图片模式错误";
            case 162:
            return "图片宽度错误";
            case 163:
            return "图片高度错误";
            case 201:
            return "缺失数据";
            case 202:
            return "用户名已被占用";
            case 203:
            return "邮箱已经被注册";
            case 204:
            return "必须提供一个邮箱地址";
            case 205:
            return "没有找到此邮件的用户";
            case 210:
            return "旧密码不正确";
        }
        return "error";
    }


}
