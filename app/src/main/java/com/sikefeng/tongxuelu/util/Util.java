package com.sikefeng.tongxuelu.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 通用工具方法类
 * 
 */
public class Util {

	
	/**
	 * 判断对象是否为空
	 * 
	 * @param o
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isEmpty(Object o) {
		if (o == null)
			return true;
		if (o instanceof String)
			return (String.valueOf(o).trim().length() == 0);
		else if (o instanceof List)
			return ((List) o).isEmpty();
		else if (o instanceof Map)
			return ((Map) o).isEmpty();
		else
			return false;
	}


	/**
	 * 判断给定字符串是否空白串。<br>
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串<br>
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */

	public static boolean isBlank(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}



	/**
	 * 使用当前时间戳拼接一个唯一的文件名
	 * 
	 * @param
	 * @return
	 */
	public static String getFileName() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SS");
		String fileName = format.format(new Timestamp(System
				.currentTimeMillis()));
		return fileName;
	}
	
	/**
	 * 保留小数点后几位
	 * 
	 * @param sum
	 *            原值
	 * @param digit
	 *            位数
	 * @return
	 */
	public static BigDecimal getscale(double sum, int digit) {
		BigDecimal bg = new BigDecimal(sum);
		BigDecimal f = bg.setScale(digit, BigDecimal.ROUND_HALF_UP);
		return f;
	}

	// 关闭键盘
	public static void KeyBoardCancle(Context context) {
		if (context == null) {
			return;
		}
		View view = ((Activity) context).getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) context
					.getSystemService(context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

	}
	


	

}
