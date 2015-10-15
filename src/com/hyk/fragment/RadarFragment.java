package com.hyk.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.NearByActivity;
import com.hyk.activity.R;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForNearestuser;
import com.hyk.user.UserInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.RoundPoint;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.xmpp.XmppApplication;

/**
 * @ClassName: RadarFragment
 * @Description: TODO(雷达)
 * @author linhs
 * @date 2014-2-21 下午3:54:26
 */
public class RadarFragment extends Fragment implements OnClickListener {

	private int currentPage = 0;
	private int pageSize = 15;
	private MediaPlayer mPlayer;
	private ACache mAcache;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private SharedPreferencesHelper spqq;
	private String regId = "0";
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.radar_fra, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAcache = ACache.get(getActivity());
//		requestQueue = Volley.newRequestQueue(getActivity());
		spqq = new SharedPreferencesHelper(getActivity(), "loginInfo");
		if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
			regId = spqq.getValue("redId");
		}
		setupViews();
		mPlayer = MediaPlayer.create(getActivity(), R.raw.bird);

	}

	public void setupViews() {
		((NearByActivity) getActivity()).the_radar_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.the_radar_btn:
			if (!NetworkUtil.checkNetworkState(getActivity())) {
				ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
			} else {
				playSound();
				if (!((NearByActivity) getActivity()).isStartRadar) { // 打开雷达
					((NearByActivity) getActivity()).startRadar();
					httpForNearestUser();
				} else { // 关闭雷达
					((NearByActivity) getActivity()).stopRadar();
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		((NearByActivity) getActivity()).stopRadar();
	}

	// 播放声音
	private void playSound() {
		try {
			if (mPlayer != null) {
				mPlayer.stop();
			}
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getNearByData(String arg0) {
		ResultStringForNearestuser resultString = new ResultStringForNearestuser();
		resultString = binder.fromJson(arg0, ResultStringForNearestuser.class);
		if (resultString != null) {
			if (resultString.getObj().getList() != null) {
				if (resultString.getObj().getList().size() > 0) {

					RoundPoint.getBigResol(resultString.getObj().getList(),
							((NearByActivity) getActivity()).iv_scan.getWidth(),
							((NearByActivity) getActivity()).iv_scan.getHeight());
					((NearByActivity) getActivity()).iv_scan.setUserLists(resultString.getObj().getList());

					((NearByActivity) getActivity()).stopRadar();
					((NearByActivity) getActivity()).startRadar();
					mAcache.remove("nearest_json");
					mAcache.put("nearest_json", arg0);
				} else {
					ToastUtil.show(getActivity(), CHString.NOT_MAN);
				}
			}
		} else {
			((NearByActivity) getActivity()).stopRadar();
			ToastUtil.show(getActivity(), CHString.NOT_MAN);
		}
	}

	/**
	 * 请求附近应用数据
	 */
	private void httpForNearestUser() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.NEARESTUSER_ADDRESS;
		Map<String, String> params = new HashMap<String, String>();
		String coordinates = mAcache.getAsString("location_key");
		if (StringUtils.isNotEmpty(coordinates)) {
			String[] latlnt = coordinates.split(",");
			params.put("lat", latlnt[0]);
			params.put("lon", latlnt[1]);
		} else {
			params.put("lat", "22.24");
			params.put("lon", "113.53");
		}
		params.put("distance", CHString.DISTANCE_INT);
		params.put("type", Httpaddress.PHONE_TYPE);
		params.put("regId", regId);
		params.put("wSize", CHString.WSIZE);
		params.put("imei", UserInfo.getUserInfo().getIMEI());
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				getNearByData(response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
				((NearByActivity) getActivity()).stopRadar();
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForNearestUser");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForNearestUser");
		} catch (Exception e) {
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForNearestUser");
		} catch (Exception e) {
		}
	}
}
