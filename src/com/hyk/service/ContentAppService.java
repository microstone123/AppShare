package com.hyk.service;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.hyk.app.ThreadUtils;
import com.hyk.app.UpdataThreadUtils;
import com.hyk.http.AppShowHttp;
import com.hyk.myapp.GetPhoneApp;
import com.hyk.utils.AppServiceManager;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;

/**
 * @ClassName: ContentAppService
 * @Description: TODO(获取应用使用情况)
 * @author linhs
 * @date 2014-2-17 下午3:40:05
 */
public class ContentAppService extends Service {
	private Timer timer = new Timer();
	private TimerTask task;
	private int startTime = 6000000; // 更新时间间隔
	private final static int START_TASK = 100031;
	private static int NUM = 0;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case START_TASK:
				new Thread() {
					public void run() {
						try {
							if (NetworkUtil.checkNetworkState(ContentAppService.this)) {
								Log.e("NetworkUtil", NUM + "");
								GetPhoneApp.getLocadApp(ContentAppService.this);
								if (NUM % 2 == 0) {
									AppShowHttp.httpForUserAppInfo(ContentAppService.this, true);
									SharedPreferencesHelper spqq = new SharedPreferencesHelper(ContentAppService.this,
											"loginInfo");
									if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
										AppShowHttp.httpForUploadUserApp(ContentAppService.this, spqq.getValue("redId"));
									}
								}
								++NUM;
							}
						} catch (OutOfMemoryError e) {
							// TODO: handle exception
						}
					};
				}.start();
				break;

			default:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if(AppServiceManager.getAppManager().getService(ContentAppService.class)!=null){
			AppServiceManager.getAppManager().finishService(this);
		}
		AppServiceManager.getAppManager().addService(this);
		
		new ThreadUtils(this).start();
		new UpdataThreadUtils(this).start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		task.cancel();
		timer.cancel();
		task = null;
		timer = null;
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				message.what = START_TASK;
				handler.sendMessage(message);
				// httpForUserAppInfo();
			}
		};
		timer.schedule(task, 0, startTime);
	}

}
