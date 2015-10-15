package com.hyk.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

public class InstalledReceiver extends BroadcastReceiver {
	private Handler handler;
	private static final int SUCCESS_UNINSTALL = 900002;
	public InstalledReceiver(Handler handler) {
		super();
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
//		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {		// install
//			String packageName = intent.getDataString();
////			Log.i("homer", "安装了 :" + packageName);
//			Toast.makeText(context, "安装了 :"+packageName, 0).show();
//		}
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {	// uninstall
			String packageName = intent.getDataString();
			Message msg = new Message();
			msg.what = SUCCESS_UNINSTALL;
			handler.sendMessage(msg);
			
		}
	}
}