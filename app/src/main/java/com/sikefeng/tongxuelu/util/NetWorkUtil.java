package com.sikefeng.tongxuelu.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @ClassName: NetWorkUtil
 * @Description:TODO(网络)
 * @author: joe
 */
public class NetWorkUtil {

	/**
	 * 判断当前网络是否可用
	 * @param act
	 * @return
	 */
	public static boolean checkNet(Activity act) {
		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}


	
}
