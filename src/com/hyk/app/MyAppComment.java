package com.hyk.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hyk.utils.CHString;
import com.hyk.utils.ToastUtil;

public class MyAppComment {
	/**
	 * 开启应用程序
	 */
	public static void startApk(Context context,String packName) {
		// 1.获取到开启这个应用的intent
		// 2. 获取开启这个应用程序第一个activity的意图
		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES);
			ActivityInfo[] activityInfos = info.activities;
			if (activityInfos != null && activityInfos.length > 0) {
				ActivityInfo activitInfo = activityInfos[0];
				Intent intent = new Intent();
				intent.setClassName(packName, activitInfo.name);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				context.startActivity(intent);
			} else {
				ToastUtil.show(context, CHString.FAILS_START+"dsfds");
			}
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.show(context, CHString.FAILS_START);
		}
	}
	
//	/**
//	 * 卸载应用程序
//	 */
//	public static void uninstallApk(Context context,boolean isUserApp,String packName) {
//		if (isUserApp) {
//			
//			Intent intent = new Intent();
//			intent.setAction("android.intent.action.VIEW");
//			intent.setAction("android.intent.action.DELETE");
//			intent.addCategory("android.intent.category.DEFAULT");
//			intent.setData(Uri.parse("package:" + packName));
//			context.startActivityForResult(intent, 200);
//			
//			
////			Intent intent = new Intent();
////			intent.setAction("android.intent.action.VIEW");
////			intent.setAction("android.intent.action.DELETE");
////			intent.addCategory("android.intent.category.DEFAULT");
////			intent.setData(Uri.parse("package:" + packName));
////			context.startActivityForResult(intent);
//		} else {
//			ToastUtil.show(context, CHString.IS_SYSAPP);
//		}
//	}
}
