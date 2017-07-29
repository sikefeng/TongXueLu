package com.sikefeng.tongxuelu.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @ClassName: ToastUtil
 * @Description:TODO(吐司工具)
 * 
 */
public class ToastUtil {

	
	private static Toast mToast;

	/**
	 * 提示消息框的提取 吐司居中显示
	 * 
	 * @param activity
	 * @param 提示消息字符串
	 */
	public static void toastInfo(Context context, String string) {
		if (mToast != null) {
			mToast.cancel();
		}
		if (Util.isEmpty(context)) {
			return;
		}
		mToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.setDuration(1500);
		mToast.show();
	}

}
