package com.sikefeng.tongxuelu.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by sikefeng on 2016/8/14.
 */
//关闭Activity的类
public class CloseActivityClass {

    public static List<Activity> activityList = new ArrayList<Activity>();

    public static void exitClient(Context ctx) {
        // 关闭所有Activity
        for (int i = 0; i < activityList.size(); i++) {
            if (null != activityList.get(i)) {
                activityList.get(i).finish();
            }
        }
        ActivityManager activityMgr = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        activityMgr.restartPackage(ctx.getPackageName());
        System.exit(0);
    }
//    然后在应用中每一个Activity的onCreate(Bundle savedInstanceState)方法中增加一句：
//            CloseActivityClass.activityList.add(this);
//    在应用退出时：
//            CloseActivityClass.exitClient(MainActivity.this);

    /**
     * Created by sikefeng on 2016/8/14.
     */
    public static Set<Activity> activities=new HashSet<Activity>();
    public static void closeActivitys(Activity activity){
        // 关闭所有Activity
        Iterator<Activity> iterator=activities.iterator();
        while (iterator.hasNext()){
               Activity ac=iterator.next();
               ac.finish();
        }
    }

}