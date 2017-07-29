package com.sikefeng.tongxuelu.util;

import org.apache.http.cookie.Cookie;

import java.util.ArrayList;
import java.util.List;

/** 
 * @ClassName: CookieUtils 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @Author: JOE
 * @Date: 2015-11-5 下午12:21:13
 */

public class CookieUtils {

	private static List<Cookie> cookies;

	public static List<Cookie> getCookies() {
		return cookies != null ? cookies : new ArrayList<Cookie>();
	}

	public static void setCookies(List<Cookie> cookies) {
		CookieUtils.cookies = cookies;
	}

	
}
