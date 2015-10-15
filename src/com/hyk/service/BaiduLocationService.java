package com.hyk.service;

import org.apache.commons.lang.StringUtils;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hyk.http.AppShowHttp;
import com.hyk.utils.ACache;
import com.hyk.utils.AppServiceManager;
import com.hyk.utils.IntentPartner;

public class BaiduLocationService extends Service {

	private static final String TAG = "BaiduLocationService";

	// public static final String ACTION_LOCATION_CHANGED
	// = "com.yeamo.service.action.location_changed";
	private ACache mAcache = null;
	public LocationClient mLocationClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public GeofenceClient mGeofenceClient;
	public boolean isAutoStopSelf = true;
	private String latitudeStr;
	private String longitudeStr;

	@Override
	public void onCreate() {
		super.onCreate();

		mLocationClient = new LocationClient(getApplicationContext());
		// mLocationClient.registerLocationListener(myListener);

		// mLocationClient = new LocationClient(this);
		/**
		 * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		 * 宸戦議AK才哘喘禰兆淫兆鰯協��泌惚聞喘壓徭失議垢殻嶄俶勣紋算葎徭失賦萩議Key
		 * ！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		 */
		mLocationClient.registerLocationListener(myListener);
		// mGeofenceClient = new GeofenceClient(this);
		mAcache = ACache.get(this);
		AppServiceManager.getAppManager().addService(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (!mLocationClient.isStarted()) {

			Log.d(TAG, "Start locate");
			LocationClientOption option = getLocationOption();
			mLocationClient.setLocOption(option);
			mLocationClient.start();
		} else {

			Log.d(TAG, "Request locate");
			mLocationClient.requestLocation();
		}

		if (intent != null) {
			isAutoStopSelf = intent.getBooleanExtra(IntentPartner.EXTRA_STOP, true);
		}

		return START_NOT_STICKY;
	}

	private LocationClientOption getLocationOption() {
		LocationClientOption option = new LocationClientOption();

		option.setOpenGps(true); // 嬉蝕gps
		option.setCoorType("gcj02"); // 譜崔恫炎窃侏
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setScanSpan(1000 * 60 * 60 * 24);
		option.setPoiNumber(10);
		option.disableCache(true);

		return option;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Stop locate");
		// mLocationClient.stop();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			if (location.getLocType() == 61 || location.getLocType() == 161) {
				setGPS();
				latitudeStr = String.valueOf(location.getLatitude());
				longitudeStr = String.valueOf(location.getLongitude());
				mAcache.put("location_key", latitudeStr + "," + longitudeStr);
			} else {
				if (StringUtils.isNotEmpty(mAcache.getAsString("location_key"))) {
					String[] str = mAcache.getAsString("location_key").split(",");
					latitudeStr = str[0];
					longitudeStr = str[1];
				} else {
					latitudeStr = "39.91667";
					longitudeStr = "116.41667";
				}
			}
			new Thread() {
				@Override
				public void run() {
					AppShowHttp.httpForUpLoadLocation(getApplicationContext(), latitudeStr, longitudeStr);
					super.run();
				}
			}.start();

			if (isAutoStopSelf) {
				stopSelf();
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
		}

	}

	public boolean setGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(BaiduLocationService.this, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
		return true;
	}

}
