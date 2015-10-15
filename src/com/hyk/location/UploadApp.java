package com.hyk.location;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;

import com.hyk.http.Httpaddress;
import com.hyk.myapp.GetPhoneApp;
import com.hyk.myapp.LocationApp;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.user.UserApp;
import com.hyk.user.UserInfo;
import com.hyk.utils.AsyncHttpUtil;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UploadApp {
	/**
	 * 上传本地应用
	 */
	public static void httpForUserAppInfo(final Context context) {
		if (NetworkUtil.checkNetworkState(context)) {
			final JsonBinder binder = JsonBinder.buildNonDefaultBinder();
			String url = Httpaddress.IP_ADDRESS + Httpaddress.UPLOADAPPINFO_ADDRESS;
			List<LocationApp> locationAppList = GetPhoneApp.loadApps(context);
			List<UserApp> appInfoList = new ArrayList<UserApp>();
			if (locationAppList != null) {
				for (LocationApp locationApp : locationAppList) {
					UserApp userApp = new UserApp();
					userApp.setAppName(locationApp.getName());
					userApp.setAppSize(locationApp.getAppSize());
					userApp.setPackageName(locationApp.getPackName());
					userApp.setAppVersion(locationApp.getAppVersion());
					appInfoList.add(userApp);
				}
			}
			RequestParams params = new RequestParams();
			params.put("imei", UserInfo.getUserInfo().getIMEI());
			params.put("userApps", binder.toJson(appInfoList));
			params.put("type", String.valueOf(Httpaddress.PHONE_TYPE));
			AsyncHttpUtil.postJson(url, params, new JsonHttpResponseHandler() {
				public void onSuccess(JSONObject arg0) { // 返回的是JSONObject，会调用这里
					ResultStringForLoadap resultString = new ResultStringForLoadap();
					resultString = binder.fromJson(arg0.toString(), ResultStringForLoadap.class);
					if (resultString != null) {
						if (resultString.getResult() != 0) {
							// httpForUserAppInfo(context);
						}
					}
				};

				public void onFailure(Throwable arg0) {
					try {
						// count++;
						// if (count < 4) {
						httpForUserAppInfo(context);
					} catch (Exception e) {
						// TODO: handle exception
					}
					// }
				};

				public void onFinish() {
					// count++;
					// if (count < 4) {
					// httpForUserAppInfo();
					// }
				};
			});
		}
	}
}
