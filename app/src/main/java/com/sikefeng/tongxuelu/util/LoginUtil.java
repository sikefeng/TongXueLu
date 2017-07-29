package com.sikefeng.tongxuelu.util;//package com.tongxuelu.util;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.xxxxtech.yaochufang.DefaultConfig;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * @ClassName: LoginUtil
// * @Description: TODO(登录工具类)
// * @Author: JOE
// * @Date: 2015-11-18 上午11:37:30
// */
//
//public class LoginUtil {
//
//
//	 /**
//     *
//    * @Title: saveUserData
//    * @Description: TODO(保存登录信息)
//    * @param loginType
//    * @param context
//    * @param jsonObject
//    * @param inputPwd
//     */
//	public static void saveUserData(final int loginType,final Context context,final JSONObject jsonObject,final String inputPwd) {
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				/** 保存登陆者的信息 */
//				try {
//					JSONObject obj = jsonObject.getJSONObject("data");
//					SPUtils.put(context, DefaultConfig.UID, obj.optString(DefaultConfig.UID));
//					System.out.println("DefaultConfig.UID="+DefaultConfig.UID);
//					SPUtils.put(context, DefaultConfig.UTYPE, obj.optString(DefaultConfig.UTYPE));
//					SPUtils.put(context, DefaultConfig.UNAME, obj.optString(DefaultConfig.UNAME));
//					SPUtils.put(context, DefaultConfig.LOGINID, obj.optString(DefaultConfig.LOGINID));
//					SPUtils.put(context, DefaultConfig.PHOTO, obj.optString(DefaultConfig.PHOTO));
//					SPUtils.put(context, DefaultConfig.STAT, obj.optString(DefaultConfig.STAT));
//					SPUtils.put(context, DefaultConfig.PHONE_NO, obj.optString(DefaultConfig.PHONE_NO));
//					SPUtils.put(context, DefaultConfig.PWD,inputPwd);
//					SPUtils.put(context, DefaultConfig.WEIXIN_NO, obj.optString(DefaultConfig.WEIXIN_NO));
//					SPUtils.put(context, DefaultConfig.SINA_WEIBO_NO, obj.optString(DefaultConfig.SINA_WEIBO_NO));
//					SPUtils.put(context, DefaultConfig.QQ_NO, obj.optString(DefaultConfig.QQ_NO));
//					SPUtils.put(context, DefaultConfig.LNG, obj.optString(DefaultConfig.LNG));
//					SPUtils.put(context, DefaultConfig.LAT, obj.optString(DefaultConfig.LAT));
//					SPUtils.put(context, DefaultConfig.ADDRESS, obj.optString(DefaultConfig.ADDRESS));
//					SPUtils.put(context, DefaultConfig.USEX, obj.optString(DefaultConfig.USEX));
//					SPUtils.put(context, DefaultConfig.LASTLOGIN, obj.optString(DefaultConfig.LASTLOGIN));
//					SPUtils.put(context, DefaultConfig.INTRO, obj.optString(DefaultConfig.INTRO));
//					SPUtils.put(context, DefaultConfig.C_NAMED, obj.optString(DefaultConfig.C_NAMED));
//					SPUtils.put(context, DefaultConfig.C_BEGOODAT, obj.optString(DefaultConfig.C_BEGOODAT));
//					SPUtils.put(context, DefaultConfig.C_CERTIFICATE_IMG, obj.optString(DefaultConfig.C_CERTIFICATE_IMG));
//					SPUtils.put(context, DefaultConfig.C_EXPERIENCE, obj.optString(DefaultConfig.C_EXPERIENCE));
//					SPUtils.put(context, DefaultConfig.PROVICE, obj.optString(DefaultConfig.PROVICE));
//					SPUtils.put(context, DefaultConfig.CITY, obj.optString(DefaultConfig.CITY));
//					SPUtils.put(context, DefaultConfig.AREA, obj.optString(DefaultConfig.AREA));
//					SPUtils.put(context, DefaultConfig.AREA_CN, obj.optString(DefaultConfig.AREA_CN));
//					SPUtils.put(context, DefaultConfig.TOKEN, obj.optString(DefaultConfig.UID)+"_"+ obj.optString(DefaultConfig.UTYPE)+"_"+obj.optString("token"));
//					Log.d(" Login ToKen "," >>>>Login ToKen =  " + SPUtils.get(context,DefaultConfig.TOKEN,"error"));
//					if(loginType != 0){
//						SPUtils.put(context, DefaultConfig.IS_THREE_LOGIN, true);//标记是否是第三方账号登录
//					}else{
//						SPUtils.put(context, DefaultConfig.IS_THREE_LOGIN, false);
//					}
//					SPUtils.put(context, DefaultConfig.ISLOGIN, true);//标记用户已经登录
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		}).start();
//	}
//
//}
