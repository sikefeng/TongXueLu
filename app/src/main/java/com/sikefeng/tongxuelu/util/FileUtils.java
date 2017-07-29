package com.sikefeng.tongxuelu.util;//package com.tongxuelu.util;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.xxxxtech.yaochufang.SApplication;
//import com.xxxxtech.yaochufang.entity.UserInfo;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.util.HashMap;
//
///**
// * @ClassName: FileUtils
// * @Description: (文件读写类)
// */
//public class FileUtils {
//	public final static String FILE_EXTENSION_SEPARATOR = ".";
//
//	private FileUtils() {
//		throw new AssertionError();
//	}
//
//	public static StringBuilder readFile(String filePath, String charsetName) {
//		File file = new File(filePath);
//		StringBuilder fileContent = new StringBuilder("");
//		if (file == null || !file.isFile()) {
//			return null;
//		}
//		BufferedReader reader = null;
//		try {
//			InputStreamReader is = new InputStreamReader(new FileInputStream(
//					file), charsetName);
//			reader = new BufferedReader(is);
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				if (!fileContent.toString().equals("")) {
//					fileContent.append("\r\n");
//				}
//				fileContent.append(line);
//			}
//			reader.close();
//			return fileContent;
//		} catch (IOException e) {
//			throw new RuntimeException("IOException occurred. ", e);
//		} finally {
//			if (reader != null) {
//				try {
//					reader.close();
//				} catch (IOException e) {
//					throw new RuntimeException("IOException occurred. ", e);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 写文件
//	 *
//	 * @param
//	 * @return boolean 返回类型
//	 */
//	public static boolean writeFile(String filePath, String content,
//			boolean append) {
//		if (TextUtils.isEmpty(content)) {
//			return false;
//		}
//
//		FileWriter fileWriter = null;
//		try {
//			makeDirs(filePath);
//			fileWriter = new FileWriter(filePath, append);
//			fileWriter.write(content);
//			fileWriter.close();
//			return true;
//		} catch (IOException e) {
//			throw new RuntimeException("IOException occurred. ", e);
//		} finally {
//			if (fileWriter != null) {
//				try {
//					fileWriter.close();
//				} catch (IOException e) {
//					throw new RuntimeException("IOException occurred. ", e);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 创建文件的路径
//	 *
//	 * @param
//	 * @return boolean 返回类型
//	 */
//	public static boolean makeDirs(String filePath) {
//		String folderName = getFolderName(filePath);
//		if (TextUtils.isEmpty(folderName)) {
//			return false;
//		}
//
//		File folder = new File(folderName);
//		return (folder.exists() && folder.isDirectory()) ? true : folder
//				.mkdirs();
//	}
//
//	public static String getFolderName(String filePath) {
//
//		if (TextUtils.isEmpty(filePath)) {
//			return filePath;
//		}
//
//		int filePosi = filePath.lastIndexOf(File.separator);
//		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
//	}
//
//	/**
//	 * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
//	 *
//	 * @param context
//
//	 */
//	public static void write(Context context, String fileName, String content) {
//		if (content == null)
//			content = "";
//
//		try {
//			FileOutputStream fos = context.openFileOutput(fileName,
//					Context.MODE_PRIVATE);
//			fos.write(content.getBytes());
//
//			fos.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 读取文本文件 文件保存在 /data/data/PACKAGE_NAME/files
//	 *
//	 * @param context
//	 * @param fileName
//	 * @return
//	 */
//	public static String read(Context context, String fileName) {
//		try {
//			FileInputStream in = context.openFileInput(fileName);
//			return readInStream(in);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
//
//	public static String readInStream(FileInputStream inStream) {
//		try {
//			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//			byte[] buffer = new byte[512];
//			int length = -1;
//			while ((length = inStream.read(buffer)) != -1) {
//				outStream.write(buffer, 0, length);
//			}
//
//			outStream.close();
//			inStream.close();
//			return outStream.toString();
//		} catch (IOException e) {
//			Log.i("FileTest", e.getMessage());
//		}
//		return null;
//	}
//
//	/**
//	 * 创建文件
//	 *
//	 * @param folderPath
//	 * @param fileName
//	 * @return
//	 */
//	public static File createFile(String folderPath, String fileName) {
//		File destDir = new File(folderPath);
//		if (!destDir.exists()) {
//			destDir.mkdirs();
//		}
//		return new File(folderPath, fileName + fileName);
//	}
//
//	/**
//	 * 把对象写入缓存中
//	 *
//	 * @param object
//	 * @param fileName
//	 * @param folder
//	 */
//	public static void write2cache(Object object, String fileName, String folder) {
//		if (Util.isEmpty(folder) || object == null || Util.isEmpty(fileName)) {
//			return;
//		}
//		String folderPath = folder + File.separator + fileName;
//		File file = new File(folderPath);
//		FileOutputStream out = null;
//		ObjectOutputStream outputStream = null;
//		try {
//			out = new FileOutputStream(file);
//			outputStream = new ObjectOutputStream(out);
//			outputStream.writeObject(object);
//			outputStream.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
////			 Log.e("write2cache", e.getMessage());
//		} finally {
//			try {
//				if (out != null)
//					out.close();
//				if (outputStream != null)
//					outputStream.close();
//			} catch (IOException e) {
////				Log.e("write2cache", e.getMessage());
//			}
//
//		}
//
//	}
//
//	public static Object readFromCache(String fileName, String folder) {
//		Object object = null;
//		if (!Util.isEmpty(folder) && !Util.isEmpty(fileName)) {
//			String folderPath = folder + File.separator + fileName;
//			File file = new File(folderPath);
//			FileInputStream out = null;
//			ObjectInputStream obi = null;
//			try {
//				out = new FileInputStream(file);
//				obi = new ObjectInputStream(out);
//				object = obi.readObject();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				object = null;
//				// Log.e("write2cache", e.getMessage());
//			} finally {
//				try {
//					if (out != null)
//						out.close();
//					if (obi != null)
//						obi.close();
//				} catch (IOException e) {
//					Log.e("write2cache", e.getMessage());
//				}
//
//			}
//		}
//		return object;
//	}
//
//	/**
//	 * 根据文件绝对路径获取文件名
//	 *
//	 * @param filePath
//	 * @return
//	 */
//	public static String getFileName(String filePath) {
//		if (Util.isBlank(filePath))
//			return "";
//		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
//	}
//
//	/**
//	 * 根据文件的绝对路径获取文件名但不包含扩展名
//	 *
//	 * @param filePath
//	 * @return
//	 */
//	public static String getFileNameNoFormat(String filePath) {
//		if (Util.isBlank(filePath)) {
//			return "";
//		}
//		int point = filePath.lastIndexOf('.');
//		return filePath.substring(filePath.lastIndexOf(File.separator) + 1,
//				point);
//	}
//
//	/**
//	 * 获取文件扩展名
//	 *
//	 * @param fileName
//	 * @return
//	 */
//	public static String getFileFormat(String fileName) {
//		if (Util.isBlank(fileName))
//			return "";
//
//		int point = fileName.lastIndexOf('.');
//		return fileName.substring(point + 1);
//	}
//
//	/**
//	 * 获取文件大小
//	 *
//	 * @param filePath
//	 * @return
//	 */
//	public static long getFileSize(String filePath) {
//		long size = 0;
//
//		File file = new File(filePath);
//		if (file != null && file.exists()) {
//			size = file.length();
//		}
//		return size;
//	}
//
//	/**
//	 * 保存至手机内存中
//	 * @param app
//	 * @param filename
//	 * @param object
//	 * @return
//	 */
//	public static void saveDataToCache(SApplication app, String filename,
//			Object object) {
//		if (app != null /* && object != null */) {
//			//filename += app.getLoginUserInfo().getUid();
//			filename = MD5Util.getMD5String(filename);
//			try {
//				FileOutputStream ops = app.openFileOutput(filename,
//						Context.MODE_PRIVATE);
//				ObjectOutputStream outputStream = null;
//				outputStream = new ObjectOutputStream(ops);
//				outputStream.writeObject(object);
//				outputStream.flush();
//				outputStream.close();
//			}catch (Exception e) {
//				Log.w("saveDataToCache file is :" + filename, e.toString());
//			}
//		}
//	}
//
//	/**
//	 * 删除文件
//	 * @param app
//	 * @param filename
//	 */
//	public static void deleteFile(SApplication app,String filename){
//		if(filename!=null){
//			//filename += app.getLoginUserInfo().getUid();
//			filename = MD5Util.getMD5String(filename);
//			File file=new File(filename);
//			if(file.exists()){
//				file.delete();
//			}
//		}
//	}
//	/**
//	 * 保存数据到缓存中
//	 * @param app
//	 * @param filename
//	 * @param object
//	 */
//	public static void saveDataToCache1(SApplication app, String filename,Object object) {
//		if (app != null && object != null) {
//			//filename += app.getLoginUserInfo().getUid();
//			filename = MD5Util.getMD5String(filename);
//			File file=new File(filename);
//			try {
//				if(file.exists()){
//					file.delete();
//					}
//				FileOutputStream ops = app.openFileOutput(filename,
//						Context.MODE_PRIVATE);
//				ObjectOutputStream outputStream = null;
//				outputStream = new ObjectOutputStream(ops);
//				outputStream.writeObject(object);
//				outputStream.flush();
//				outputStream.close();
//			}catch (Exception e) {
//				Log.w("saveDataToCache file is :" + filename, e.toString());
//			}
//		}
//	}
//	/**
//	 * 读取缓存
//	 * @param app
//	 * @param filename
//	 * @return
//	 */
//	public static Object getDataFromCache(SApplication app, String filename) {
//		Object obj = null;
//		if (app != null ) {
//			//filename += app.getLoginUserInfo().getUid();
//			filename = MD5Util.getMD5String(filename);
//			try {
//				InputStream ies = app.openFileInput(filename);
//				ObjectInputStream obi = new ObjectInputStream(ies);
//				obj = obi.readObject();
//				obi.close();
//				ies.close();
//			} catch (Exception e) {
//				Log.w("getDataFromCache file is :" + filename, e.toString());
//			}
//		}
//		return obj;
//	}
//
//	/**
//	 * 保存用户数据
//	 *
//	 * @param context
//	 * @param user
//	 */
//	public static void saveuserInfo(Context context, UserInfo user) {
//
//		String filename = "userinfo";
//		filename = MD5Util.getMD5String(filename);
//		try {
//			HashMap<String, Object> parm = new HashMap<String, Object>();
//			parm.put("uu", user);
//			FileOutputStream ops = context.openFileOutput(filename,
//					Context.MODE_PRIVATE);
//			ObjectOutputStream outputStream = null;
//			outputStream = new ObjectOutputStream(ops);
//			outputStream.writeObject(parm);
//			outputStream.flush();
//			outputStream.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * 读取用户数据
//	 * @param context
//	 * @return
//	 */
//	public static UserInfo readuserInfo(Context context) {
//
//		String filename = "userinfo";
//		filename = MD5Util.getMD5String(filename);
//		try {
//			InputStream ies = context.openFileInput(filename);
//			ObjectInputStream obi = new ObjectInputStream(ies);
//			// UserEntity tmpuser = (UserEntity)obi.readObject();
//			Object obj = obi.readObject();
//			obi.close();
//			ies.close();
//			if (obj != null) {
//				if (obj instanceof HashMap<?, ?>) {
//					@SuppressWarnings("unchecked")
//					HashMap<String, Object> parm = (HashMap<String, Object>) obj;
//					UserInfo tmpuser = (UserInfo) parm.get("uu");
//					return tmpuser;
//				}
//			}
//		} catch (Exception e) {
//			UserInfo temp = new UserInfo();
//			temp.setUid("0");
////			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	/**
//	 * 递归删除文件和文件夹
//	 * @param file
//	 *要删除的根目录
//	 */
//	public static void RecursionDeleteFile(File file) {
//		if (file.isFile()) {
//			file.delete();
//			return;
//		}
//		if (file.isDirectory()) {
//			File[] childFile = file.listFiles();
//			if (childFile == null || childFile.length == 0) {
//				// file.delete();
//				return;
//			}
//			for (File f : childFile) {
//				RecursionDeleteFile(f);
//			}
//			file.delete();
//		}
//	}
//
//}
