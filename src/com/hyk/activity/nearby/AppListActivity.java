package com.hyk.activity.nearby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.R;
import com.hyk.baseadapter.AppListAdapter;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForNearestuser;
import com.hyk.user.UserInfo;
import com.hyk.user.UserList;
import com.hyk.utils.ACache;
import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.PullListView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;

/**
 * @ClassName: AppListActivity
 * @Description: TODO(附近用户列表)
 * @author linhs
 * @date 2014-2-19 下午5:43:09
 */
public class AppListActivity extends FragmentActivity implements PullListView.OnRefreshLoadingMoreListener,
		OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;

	public ImageButton btn_back;
	private PullListView applist_view;

	private ACache mAcache;
	private List<UserList> appsList = new ArrayList<UserList>();
	private AppListAdapter appListAdapter;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private List<UserList> appsStaticList = new ArrayList<UserList>();

	private int currentPage = 0;
	private int pageSize = 15;
	private int total = 0;
	private String regId = "0";
	private SharedPreferencesHelper spqq;
	private boolean isFirst = true;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private boolean isReft = true;
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.applist);
		AppManager.getAppManager().addActivity(this);
		spqq = new SharedPreferencesHelper(this, "loginInfo");
		if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
			regId = spqq.getValue("redId");
		}
		setupViews();
		stopLoad();
		mAcache = ACache.get(this);
		isFirst = true;
		getNearList();
	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);
		applist_view = (PullListView) findViewById(R.id.applist_view);
		applist_view.setOnRefreshListener(this);
	}

	@Override
	protected void onResume() {

		// if (!isFirst) {
		// startLoad();
		// }
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		super.onResume();
	}

	/**
	 * 添加我的身边数据
	 */
	private void getNearList() {
		JSONObject hotJson = mAcache.getAsJSONObject("nearest_json");
		if (hotJson != null) {
			appsList = setAppList(hotJson.toString());
			if (appsList != null) {
				appListAdapter = new AppListAdapter(AppListActivity.this, regId, appsList);
				applist_view.setAdapter(appListAdapter);
			}
		} else {
			currentPage = 0;
			isReft = true;
			httpForNearestUser();
		}
	}

	/**
	 * 获取附近app列表
	 */
	public List<UserList> setAppList(String arg0) {
		ResultStringForNearestuser resultString = new ResultStringForNearestuser();
		resultString = binder.fromJson(arg0, ResultStringForNearestuser.class);
		if (resultString != null) {
			if (resultString.getObj() != null) {
				total = resultString.getObj().getTotal();
				if (isReft) {
					mAcache.put("nearest_json", arg0);
				}
			} else {
				if (isFirst) {
					setAppList(mAcache.getAsString("nearest_json"));
				}
			}
			return romeNullApp(resultString.getObj().getList());
		} else {
			if (isFirst) {
				if (StringUtils.isNotEmpty(mAcache.getAsString("nearest_json"))) {
					return setAppList(mAcache.getAsString("nearest_json"));
				} else {
					return null;
				}

			} else {
				return null;
			}
		}
	}

	/**
	 * 去除为空的app用户
	 */
	private List<UserList> romeNullApp(List<UserList> list) {
		if (list != null) {
			List<UserList> newList = new ArrayList<UserList>();
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getApps() != null) {
					if (list.get(i).getApps().size() > 0) {
						appsStaticList.add(list.get(i));
						newList.add(list.get(i));
					}
				}
			}
			return newList;
		} else {
			return null;
		}

	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, this);
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRefresh() {
		currentPage = 0;
		isReft = true;
		httpForNearestUser();
	}

	@Override
	public void onLoadMore() {
		++currentPage;
		isReft = false;
		int totalPage = total / pageSize;
		int pageY = total % pageSize;
		if (pageY != 0) {
			++totalPage;
		}
		--totalPage;
		if (currentPage <= totalPage) {
			httpForNearestUser();
		} else {
			ToastUtil.show(this, CHString.THE_AFTER_PAGE);
			applist_view.onLoadMoreComplete(false);
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		isFirst = false;
		XmppApplication.getsInstance().cancelPendingRequests("httpForNearestUser");
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		onRefresh();
	}

	// private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
	// @Override
	// protected String doInBackground(String... params) {
	// theMark = ++httpCode;
	// String jsonString = AppShowHttp.httpForNearestUser(AppListActivity.this,
	// regId, currentPage, pageSize);
	// if (StringUtils.isNotEmpty(jsonString)) {
	// appsList = setAppList(jsonString);
	// } else {
	// if (!isReft) {
	// --currentPage;
	// }
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// try {
	// if (theMark == httpCode) {
	// stopLoad();
	// if (isReft) {
	// if (appsList != null) {
	// appListAdapter = new AppListAdapter(AppListActivity.this, appsList);
	// applist_view.setAdapter(appListAdapter);
	// }
	// } else {
	// for (int i = 0; i < appsList.size(); i++) {
	// appListAdapter.addUserList(appsList.get(i));
	// }
	// }
	// applist_view.onRefreshComplete();
	// applist_view.onLoadMoreComplete(false);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	//
	// @Override
	// public void onPreExecute() {
	//
	// }
	// }

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
				try {
					appsList = setAppList(response);
					stopLoad();
					if (isReft) {
						if (appsList != null) {
							appListAdapter = new AppListAdapter(AppListActivity.this, regId, appsList);
							applist_view.setAdapter(appListAdapter);
						}
					} else {
						for (int i = 0; i < appsList.size(); i++) {
							appListAdapter.addUserList(appsList.get(i));
						}
					}
					applist_view.onRefreshComplete();
					applist_view.onLoadMoreComplete(false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				try {
					stopLoad();
					if (!isReft) {
						--currentPage;
					}
					ToastUtil.show(AppListActivity.this, CHString.NETWORK_BUSY);
					applist_view.onRefreshComplete();
					applist_view.onLoadMoreComplete(false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
//		jsonObjectRequest.setTag("httpForNearestUser");
//		requestQueue.add(jsonObjectRequest);
		
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForNearestUser");
	}

}
