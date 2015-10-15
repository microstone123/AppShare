package com.hyk.fragment.nearby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.activity.nearby.UserInfoActivity;
import com.hyk.baseadapter.MyScrollHandler;
import com.hyk.baseadapter.UserAppListAdapter;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForUserApp;
import com.hyk.user.UserAppInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.ToastUtil;
import com.hyk.view.PullListView;
import com.hyk.xmpp.XmppApplication;

/**
 * @ClassName: UserAppActivity
 * @Description: TODO(个人应用)
 * @author linhs
 * @date 2014-3-10 下午3:56:07
 */
public class UserAppFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener {

	private PullListView recen_gridview;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private UserAppListAdapter userAppListAdapter;
	private ACache mACache;
	private int currentPage = 1;
	private int pageSize = 15;
	private boolean isDropDown = true;
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private MyScrollHandler scrollHandler;
	private int total;
	private List<UserAppInfo> userAppInfo = new ArrayList<UserAppInfo>();
	// private List<UserAppInfo> userAppInfoList = new ArrayList<UserAppInfo>();
	// private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;
	public String myId, regId, rImei;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// TextView viewhello = (TextView) view.findViewById(R.id.tv_hello);
		// viewhello.setText(maplist.get("userid")+"time");
		return inflater.inflate(R.layout.user_app, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mACache = ACache.get(getActivity());
		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.loading_relat);
		loading_img = (ImageView) getActivity().findViewById(R.id.loading_img);
		recen_gridview = (PullListView) getActivity().findViewById(R.id.userapp_listview);
		recen_gridview.setSelector(R.drawable.hide_listview_yellow_selector);
		recen_gridview.setOnRefreshListener(this);
		myId = ((UserInfoActivity) getActivity()).myId;
		regId = ((UserInfoActivity) getActivity()).redId;
		rImei = ((UserInfoActivity) getActivity()).rImei;
		scrollHandler = new MyScrollHandler(recen_gridview, null, null);
		startLoad();
	}

	@Override
	public void onRefresh() {
		isDropDown = true;
		currentPage = 1;
		httpForUserApp();
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		int countNum = total / pageSize;
		int yushu = total % pageSize;
		if (yushu != 0) {
			countNum++;
		}
		if (currentPage <= countNum) {
			isDropDown = false;
			httpForUserApp();
		} else {
			recen_gridview.onLoadMoreComplete(false);
			ToastUtil.show(getActivity(), CHString.THE_AFTER_PAGE);
		}
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		recen_gridview.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		recen_gridview.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		onRefresh();
	}

	private List<UserAppInfo> getUserApp(String jsonString) {
		List<UserAppInfo> list = new ArrayList<UserAppInfo>();
		ResultStringForUserApp resultString = new ResultStringForUserApp();
		resultString = binder.fromJson(jsonString, ResultStringForUserApp.class);
		if (resultString != null) {
			if (resultString.getObj() != null) {
				total = resultString.getObj().getTotal();
				if (isDropDown) {
					mACache.put("user_app_data_string", jsonString);
				}
			} else {
				list = null;
			}
			list = resultString.getObj().getList();
		} else {
			list = null;
		}
		return list;
	}

	@Override
	public void onResume() {
		try {
			userAppListAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		// requestQueue.cancelAll("httpForUserApp");
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForUserApp");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForUserApp");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 请求用户app
	 */
	private void httpForUserApp() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APP_USERAPP;
		Map<String, String> params = new HashMap<String, String>();
		String imei = mACache.getAsString("user_imei");
		params.put("imei", imei);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				stopLoad();
				userAppInfo = getUserApp(response);
				if (userAppInfo != null) {
					if (userAppInfo.size() > 0) {
						if (isDropDown) {
							userAppListAdapter = new UserAppListAdapter(getActivity(), userAppInfo, scrollHandler,
									myId, regId, rImei);
							recen_gridview.setAdapter(userAppListAdapter);
						} else {
							if (userAppInfo != null) {
								if (userAppInfo.size() > 0) {
									for (int i = 0; i < userAppInfo.size(); i++) {
										userAppListAdapter.addUserAppInfo(userAppInfo.get(i));
									}
									// userAppInfoList.addAll(userAppInfo);
								}
							}
						}
					}
				} else {
					ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
				}
				recen_gridview.onRefreshComplete();
				recen_gridview.onLoadMoreComplete(false);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				stopLoad();
				if (!isDropDown) {
					--currentPage;
				} else {
					String jsonString = mACache.getAsString("user_app_data_string");
					if (StringUtils.isNotEmpty(jsonString)) {
						userAppInfo = getUserApp(jsonString);
						userAppListAdapter = new UserAppListAdapter(getActivity(), userAppInfo, scrollHandler, myId,
								regId, rImei);
						recen_gridview.setAdapter(userAppListAdapter);
					}
				}
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest,"httpForUserApp");
	}

}
