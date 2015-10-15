package com.hyk.activity;

import org.apache.commons.lang.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

import com.hyk.http.CheckVersionTask;
import com.hyk.service.BaiduLocationService;
import com.hyk.service.ContentAppService;
import com.hyk.user.UserInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.AppManager;
import com.hyk.utils.SIMCardInfo;
import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;

public class WelcomeActivity extends Activity {

	
	private ACache mAcache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.welcome);
		mAcache = ACache.get(this);
		
//		AsyncTaskUtil AsyncTaskUtil = new AsyncTaskUtil();
//		AsyncTaskUtil.execute();
		
		AppManager.getAppManager().addActivity(this);
//		XmppConnection.setContext(this);
		CheckSIM();
		
//		//登陆判断
//		new Thread(new CheckVersionTask(this)).start();
//		startService(new Intent(this, ContentAppService.class));
//		// 获取经纬度与上传经纬度
//		startService(new Intent(this, BaiduLocationService.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}
	
	
	/**
	 * 获取SIM
	 */
	private void CheckSIM() {
		TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String operator = telManager.getSubscriberId();
		if (StringUtils.isNotEmpty(operator)) {
			SIMCardInfo siminfo = new SIMCardInfo(this);
			UserInfo.getUserInfo().setIMSI(siminfo.getIMSI(this));
			UserInfo.getUserInfo().setPhoneType(siminfo.getInfoTYPE(this));
		}
		UserInfo.getUserInfo().setIMEI(telManager.getDeviceId());
		getScreenInfo();
	}

	/**
	 * 获取手机分辨率
	 */
	@SuppressWarnings("deprecation")
	private void getScreenInfo() {
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
		int height = wm.getDefaultDisplay().getHeight();
		mAcache.put("screen_height", String.valueOf(height));
		mAcache.put("screen_width", String.valueOf(width));
	}

	private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			try {
				// Log.e("doInBackground", "doInBackground");
				XmppApplication.getsInstance().setConnten(getApplicationContext());
				Log.e("doInBackground", XmppConnection.isConnected() + "");
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// appGridAdapter = new MoreAppListAdapter(getActivity(),
			// oldRecentlyApp, handler);
			// recen_listview.setAdapter(appGridAdapter);
			// recen_listview.onRefreshComplete();
			// stopLoad();
		}

		@Override
		public void onPreExecute() {

		}
	}
	
}
