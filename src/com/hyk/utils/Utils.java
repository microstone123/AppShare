package com.hyk.utils;

import android.os.Build;

public class Utils {
//	@TargetApi(11)
//	public static void enableStrictMode() {
//		if (hasGingerBread()) {
//			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
//			StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();
//			if (hasHoneyComb()) {
//				threadPolicyBuilder.penaltyFlashScreen();
//				vmPolicyBuilder.setClassInstanceLimit(MainActivity.class, 1);
//			}
//			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
//			StrictMode.setVmPolicy(vmPolicyBuilder.build());
//		}
//	}

	public static boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerBread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasGingerBreadPlus() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
	}
	
	public static boolean hasHoneyComb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}
	
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}
}