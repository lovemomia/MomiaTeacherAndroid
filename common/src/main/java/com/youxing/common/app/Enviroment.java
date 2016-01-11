package com.youxing.common.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

import com.youxing.common.utils.Log;

public class Enviroment {

	private static String versionName;
	private static int versionCode;
	private static String deviceType;

	private static int screenWidth;

	public static String versionName() {
		if (versionName == null) {
			readPackageInfo();
		}
		return versionName;
	}

	public static int versionCode() {
		if (versionCode == 0) {
			readPackageInfo();
		}
		return versionCode;
	}

	private static void readPackageInfo() {
		try {
			PackageInfo pinfo = YXApplication
					.instance()
					.getPackageManager()
					.getPackageInfo(
							YXApplication.instance().getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
			versionName = pinfo.versionName;
			versionCode = pinfo.versionCode;
		} catch (NameNotFoundException e) {
		}
	}

	public static String deviceType() {
		if (deviceType == null) {
			try {
				deviceType = Build.MODEL.replace(" ", "");
			} catch (Exception e) {
				deviceType = "unkown";
			}
		}
		return deviceType;
	}

	public static int screenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if (screenWidth == 0) {
			screenWidth = wm.getDefaultDisplay().getWidth();
		}
		return screenWidth;
	}

	/**
	 * 获取网络类型（wifi）
	 */
	private static ConnectivityManager connManager;
	public static String getNetworkType() {
		if (connManager == null) {
			ConnectivityManager cm = null;
			try {
				cm = (ConnectivityManager) YXApplication.instance()
						.getSystemService(Context.CONNECTIVITY_SERVICE);
			} catch (Exception e) {
				Log.w("env", "connectivity manager init fail",
						e);
			}
			connManager = cm;
		}
		try {
			NetworkInfo info = connManager.getActiveNetworkInfo();
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				switch (info.getSubtype()) {
					case TelephonyManager.NETWORK_TYPE_1xRTT:// ~ 50-100 kbps
					case TelephonyManager.NETWORK_TYPE_CDMA:// ~ 14-64 kbps
					case TelephonyManager.NETWORK_TYPE_EDGE:// ~ 50-100 kbps
					case TelephonyManager.NETWORK_TYPE_GPRS:// ~ 100 kbps
					case TelephonyManager.NETWORK_TYPE_IDEN:// ~25 kbps
						return "2G";
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B:
					case TelephonyManager.NETWORK_TYPE_EHRPD:
					case TelephonyManager.NETWORK_TYPE_HSPAP:
						return "3G";
					case TelephonyManager.NETWORK_TYPE_LTE:
						return "4G";
					default:
						return "unknown";
				}
			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				return "wifi";
			}
		} catch (Exception e) {
		}
		return "unknown";
	}

	private static String osInfo;
	public static String osInfo() {
		if (osInfo == null) {
			osInfo = "SDK" + Build.VERSION.SDK_INT;
		}
		return osInfo;
	}

	/**
	 * 渠道号
	 *
	 * @return
	 */
	private static String channel;
	public static String channel() {
		if (channel == null) {
			try {
				ApplicationInfo info = YXApplication.instance().getPackageManager()
						.getApplicationInfo(YXApplication.instance().getPackageName(),
								PackageManager.GET_META_DATA);
				channel = info.metaData.getString("UMENG_CHANNEL");

			} catch (Exception e) {
				channel = "unknown";
			}
		}
		return channel;
	}

}
