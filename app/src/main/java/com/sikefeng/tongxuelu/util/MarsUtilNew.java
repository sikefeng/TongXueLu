package com.sikefeng.tongxuelu.util;//package com.tongxuelu.util;
//
//
//
//
//public class MarsUtilNew {
//	private static final double PI = 3.14159265358979324;
//	private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
//
//	// const double delta = function (lat, lon)
//	public static Point delta(double lat, double lon) {
//		double dLat;
//		double dLon;
//		// Krasovsky 1940
//		//
//		// a = 6378245.0, 1/f = 298.3
//		// b = a * (1 - f)
//		// ee = (a^2 - b^2) / a^2;
//		double a = 6378245.0; // a: 卫星椭球坐标投影到平面地图坐标系的投影因子。
//		float ee = 0.00669342162296594323f; // ee: 椭球的偏心率。
//		dLat = transformLat(lon - 105.0, lat - 35.0);
//		dLon = transformLon(lon - 105.0, lat - 35.0);
//		float radLat = (float) (lat / 180.0 * PI);
//		double magic = Math.sin(radLat);
//		magic = 1 - ee * magic * magic;
//		double sqrtMagic = Math.sqrt(magic);
//		dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * PI);
//		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
//		return new Point(dLat, dLon);
//	}
//
//	// WGS-84 to GCJ-02
//	public static Point gcj_encrypt(double wgsLat, double wgsLon) {
//		double dwgsLat;
//		double dwgsLon;
//		if (outOfChina(wgsLat, wgsLon)) {
//			dwgsLat = wgsLat;
//			dwgsLon = wgsLon;
//		}
//
//		Point p = delta(wgsLat, wgsLon);
//		dwgsLat = wgsLat + p.getLat();
//		dwgsLon = wgsLon + p.getLng();
//		return new Point(dwgsLat, dwgsLon);
//	}
//
//	// GCJ-02 to WGS-84
//	private static Point gcj_decrypt(double gcjLat, double gcjLon) {
//		double dgcjLat;
//		double dgcjLon;
//		if (outOfChina(gcjLat, gcjLon)) {
//			dgcjLat = gcjLat;
//			dgcjLon = gcjLon;
//
//		}
//		Point p = delta(gcjLat, gcjLon);
//		dgcjLat = gcjLat - p.getLat();
//		dgcjLon = gcjLon - p.getLng();
//		return new Point(dgcjLat, dgcjLon);
//	}
//
//	// GCJ-02 to WGS-84 exactly
//	public static Point gcj_decrypt_exact(double gcjLat, double gcjLon) {
//		double wgsLat;
//		double wgsLon;
//		double initDelta = 0.01;
//		float threshold = 0.000000001f;
//		double dLat = initDelta, dLon = initDelta;
//		double mLat = gcjLat - dLat, mLon = gcjLon - dLon;
//		double pLat = gcjLat + dLat, pLon = gcjLon + dLon;
//		double i = 0;
//		while (true) {
//			wgsLat = (mLat + pLat) / 2;
//			wgsLon = (mLon + pLon) / 2;
//			Point p = gcj_encrypt(wgsLat, wgsLon);
//			dLat = p.getLat() - gcjLat;
//			dLon = p.getLng() - gcjLon;
//			if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold))
//				break;
//
//			if (dLat > 0)
//				pLat = wgsLat;
//			else
//				mLat = wgsLat;
//			if (dLon > 0)
//				pLon = wgsLon;
//			else
//				mLon = wgsLon;
//
//			if (++i > 10000)
//				break;
//		}
//		return new Point(wgsLat, wgsLon);
//
//	}
//
//	// GCJ-02 to BD-09
//	public static Point bd_encrypt(double gcjLat, double gcjLon) {
//		double bdLat;
//		double bdLon;
//		double x = gcjLon;
//		double y = gcjLat;
//		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
//		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
//		bdLon = z * Math.cos(theta) + 0.0065;
//		bdLat = z * Math.sin(theta) + 0.006;
//		return new Point(bdLat, bdLon);
//	}
//
//	// BD-09 to GCJ-02
//	public static Point bd_decrypt(double bdLat, double bdLon) {
//		double gcjLat;
//		double gcjLon;
//		double x = bdLon - 0.0065;
//		double y = bdLat - 0.006;
//		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
//		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
//		gcjLon = z * Math.cos(theta);
//		gcjLat = z * Math.sin(theta);
//		return new Point(gcjLat, gcjLon);
//	}
//
//	// WGS-84 to Web mercator
//	// mercatorLat -> y mercatorLon -> x
//	public static Point mercator_encrypt(double wgsLat, double wgsLon) {
//		double lonx;
//		double laty;
//		lonx = wgsLon * 20037508.34 / 180.0;
//		laty = Math.log(Math.tan((90.0 + wgsLat) * PI / 360.0)) / (PI / 180.0);
//		laty = laty * 20037508.34 / 180.0;
//		return new Point(lonx, laty);
//		/*
//		 * if ((Math.abs(wgsLon) > 180 || Math.abs(wgsLat) > 90)) return null;
//		 * var x = 6378137.0 * wgsLon * 0.017453292519943295; var a = wgsLat *
//		 * 0.017453292519943295; var y = 3189068.5 * Math.log((1.0 +
//		 * Math.Sin(a)) / (1.0 - Math.Sin(a))); return {'lat' : y, 'lon' : x};
//		 * //
//		 */
//	}
//
//	// Web mercator to WGS-84
//	// mercatorLat -> y mercatorLon -> x
//	public static Point mercator_decrypt(double mercatorLat, double mercatorLon) {
//		double lonx;
//		double laty;
//		lonx = mercatorLon / 20037508.34 * 180.0;
//		laty = mercatorLat / 20037508.34 * 180.0;
//		laty = 180 / PI * (2 * Math.atan(Math.exp(laty * PI / 180.0)) - PI / 2);
//		return new Point(lonx, laty);
//		/*
//		 * if (Math.abs(mercatorLon) < 180 && Math.abs(mercatorLat) < 90) return
//		 * null; if ((Math.abs(mercatorLon) > 20037508.3427892) ||
//		 * (Math.abs(mercatorLat) > 20037508.3427892)) return null; var a =
//		 * mercatorLon / 6378137.0 * 57.295779513082323; var x = a -
//		 * (Math.floor(((a + 180.0) / 360.0)) * 360.0); var y =
//		 * (1.5707963267948966 - (2.0 * Math.atan(Math.exp((-1.0 * mercatorLat)
//		 * / 6378137.0)))) * 57.295779513082323; return {'lat' : y, 'lon' : x};
//		 * //
//		 */
//	}
//
//	// two point's distance
//	public static double distance(double latA, double lonA, double latB,
//			double lonB) {
//		double earthR = 6371000.0;
//		double x = Math.cos(latA * PI / 180.0) * Math.cos(latB * PI / 180.0)
//				* Math.cos((lonA - lonB) * PI / 180);
//		double y = Math.sin(latA * PI / 180.0) * Math.sin(latB * PI / 180.0);
//		double s = x + y;
//		if (s > 1)
//			s = 1;
//		if (s < -1)
//			s = -1;
//		double alpha = Math.acos(s);
//		double distance = alpha * earthR;
//		return distance;
//	}
//
//	public static boolean outOfChina(double lat, double lon) {
//		if (lon < 72.004 || lon > 137.8347)
//			return true;
//		if (lat < 0.8293 || lat > 55.8271)
//			return true;
//		return false;
//	}
//
//	public static double transformLat(double x, double y) {
//		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
//				+ 0.2 * Math.sqrt(Math.abs(x));
//		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
//		ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
//		ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
//		return ret;
//	}
//
//	public static double transformLon(double x, double y) {
//		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1
//				* Math.sqrt(Math.abs(x));
//		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
//		ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
//		ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0
//				* PI)) * 2.0 / 3.0;
//		return ret;
//	}
//
//	public static void main(String[] args) {
//		double lng = 144.345693;
//		double lat = 22.345678;
//		Point p = LatlngToMars("Baidu", lng, lat);
//		System.out.println(p.getLat() + "    " + p.getLng());
//	}
//
//	// / <summary>
//	// / 火星坐标系转成正常的坐标系
//	// / </summary>
//	// / <param name="offsetName">自定义名称,用于区分偏移内容,例如google、baidu</param>
//	// / <param name="lng">火星经度</param>
//	// / <param name="lat">火星纬度</param>
//	// / <returns>正常的经纬度</returns>
//	public static Point MarsToLatlng(String offsetName, double lng, double lat) {
//
//		if (offsetName == "Baidu") {
//			Point pp = MarsUtilNew.bd_decrypt(lat, lng);
//			lat = pp.getLat();
//			lng = pp.getLng();
//		}
//		Point p = MarsUtilNew.gcj_decrypt_exact(lat, lng);
//		return p;
//	}
//
//	// / <summary>
//	// / 正常的经纬度转为火星坐标系
//	// / </summary>
//	// / <param name="offsetName">自定义名称,用于区分偏移内容,例如google、baidu</param>
//	// / <param name="lng">正常经度</param>
//	// / <param name="lat">正常纬度</param>
//	// / <returns>火星经纬度</returns>
//	public static Point LatlngToMars(String offsetName, double lng, double lat) {
//		if (offsetName == "Baidu") {
//			Point pp = MarsUtilNew.bd_encrypt(lat, lng);
//			lat = pp.getLat();
//			lng = pp.getLng();
//		}
//		Point p = MarsUtilNew.gcj_encrypt(lat, lng);
//		return p;
//	}
//}
