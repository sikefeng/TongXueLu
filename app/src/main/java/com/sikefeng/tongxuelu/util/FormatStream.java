package com.sikefeng.tongxuelu.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 把字符流转换字符串的工具类
 * @author Administrator
 *
 */
public class FormatStream {
	/**
	 * 把字符流转换字符串
	 * 
	 * @param inSream
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String readData(InputStream inSream, String charsetName)
			throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		return new String(data, charsetName);
	}
	
}
