package com.hyk.utils;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.Uri;

public class NetworkUtil {
	/**
	 * 检测网络是否连接 false:已连接 true:未连接
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static boolean checkNetworkState(Context context) {
		boolean flag = false;
		try {
			// 得到网络连接信息
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
			// 去进行判断网络是否连接
			if (manager.getActiveNetworkInfo() != null) {
				flag = manager.getActiveNetworkInfo().isAvailable();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return flag;
	}

	@SuppressWarnings("static-access")
	public static boolean checkGPSState(Context context) {
		boolean flag = false;
		LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		/**
		 * GPS_PROVIDER 代表gps信息由GPS提供 NETWORK_PRIVODER 代表信息通过网络提供
		 * PASSIVE_PROVIDER 代表信息由特殊设备提供，用的比较少
		 * */
		LocationProvider lp = manager.getProvider(LocationManager.GPS_PROVIDER);
		if (lp != null) {
			flag = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!flag) {
				flag = setGPS(context);
			}
		} else {
			flag = false;
		}
		return flag;
	}

	/**
	 * 打开GPS
	 */
	public static boolean setGPS(Context contxt) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(contxt, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
		return true;
	}
}
