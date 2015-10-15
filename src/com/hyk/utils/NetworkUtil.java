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
	 * ��������Ƿ����� false:������ true:δ����
	 * 
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static boolean checkNetworkState(Context context) {
		boolean flag = false;
		try {
			// �õ�����������Ϣ
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
			// ȥ�����ж������Ƿ�����
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
		 * GPS_PROVIDER ����gps��Ϣ��GPS�ṩ NETWORK_PRIVODER ������Ϣͨ�������ṩ
		 * PASSIVE_PROVIDER ������Ϣ�������豸�ṩ���õıȽ���
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
	 * ��GPS
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
