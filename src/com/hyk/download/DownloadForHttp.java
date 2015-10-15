package com.hyk.download;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyk.http.Httpaddress;
import com.hyk.utils.HttpUtils;
import com.hyk.xmpp.XmppApplication;

public class DownloadForHttp {
	private static StringRequest jsonObjectRequest;

	/**
	 * ÏÂÔØappÍ¨Öª
	 */
	public static void httpForDownapp(String appId,String appUserId,String downUserId,String imei,String appImei){
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_DOWNAPP;
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("appUserId", appUserId);
		params.put("downUserId", downUserId);
		params.put("downUserImei", imei);
		params.put("appImei", appImei);
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e("response", response);
				
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				
			}
		});
//		jsonObjectRequest.setTag("httpForDownapp");
//		requestQueue.add(jsonObjectRequest);
		
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForDownapp");

	}
}
