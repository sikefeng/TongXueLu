package com.sikefeng.tongxuelu.activity.user;

import android.content.Context;

import cn.bmob.v3.BmobUser;

/**
 * Created by sikefeng on 2016/8/12.
 */
public class UserUtils {

    private static User loginUser = null;
    public UserUtils(Context context) {
        if (loginUser==null){
            loginUser = BmobUser.getCurrentUser(context, User.class); // 获取已登录对象和信息
        }
    }

    public User getLoginUser(){
            return loginUser;
    }

    public static User getLoginUser(Context context){
        if (loginUser==null){
            loginUser = BmobUser.getCurrentUser(context, User.class); // 获取已登录对象和信息
        }
        return loginUser;
    }

}
