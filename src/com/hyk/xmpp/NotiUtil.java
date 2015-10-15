package com.hyk.xmpp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.hyk.activity.center.set.AnnouncementActivity;
import com.hyk.utils.SharedPreferencesHelper;

public class NotiUtil {
	@SuppressWarnings("deprecation")
	public static void setNotiType(Context context, int iconId, String note) {
		SharedPreferencesHelper spqq = new SharedPreferencesHelper(context, "loginInfo");
		spqq.putStrValue("announcementStr", note);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Service.NOTIFICATION_SERVICE);
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setClass(context, AnnouncementActivity.class);
		// 触发界面跳转
		PendingIntent appIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
				| PendingIntent.FLAG_ONE_SHOT);

		Notification myNoti = new Notification();
		myNoti.icon = iconId;
		myNoti.tickerText = note;
		myNoti.defaults = Notification.DEFAULT_SOUND;
		myNoti.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动消失
		myNoti.setLatestEventInfo(context, "系统公告", note, appIntent);
		mNotificationManager.notify(0, myNoti);
	}
}
