package com.hyk.app;

import android.content.Context;

import com.hyk.http.AppShowHttp;
import com.hyk.utils.NetworkUtil;

public class ThreadUtils extends Thread {
	private Context context;

	public ThreadUtils(Context context) {
		super();
		this.context = context;
	}
	@Override
	public void run() {
		if (NetworkUtil.checkNetworkState(context)) {
			AppShowHttp.httpForHotApp(context);
		}
	}
}
