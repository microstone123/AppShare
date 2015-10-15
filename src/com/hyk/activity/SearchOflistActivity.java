package com.hyk.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.app.AppDetailsActivity;
import com.hyk.baseadapter.SearchListAdapter;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForNearestuser;
import com.hyk.resultforjson.ResultStringForSearch;
import com.hyk.user.UserAppInfo;
import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
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
public class SearchOflistActivity extends FragmentActivity implements PullListView.OnRefreshLoadingMoreListener,
		OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;

	public ImageButton btn_back;
	private PullListView applist_view;

	private List<UserAppInfo> appsList = new ArrayList<UserAppInfo>();
	private SearchListAdapter appListAdapter;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();

	private int currentPage = 0;
	private int pageSize = 15;
	private boolean isDropDown = true;
	private int total = 0;
	private String appName;
	// private LoadDialog loadDialog;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	
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
		setContentView(R.layout.search_list);
//		requestQueue = Volley.newRequestQueue(this);
		AppManager.getAppManager().addActivity(this);
		appName = getIntent().getExtras().getString("app_name");
		setupViews();
		startLoad();
	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());
		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);

		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		applist_view = (PullListView) findViewById(R.id.search_list_view);
		applist_view.setOnRefreshListener(this);

		applist_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewSetClick.getAlpha(arg1, SearchOflistActivity.this);
				Intent intent = new Intent(SearchOflistActivity.this, AppDetailsActivity.class);
				intent.putExtra("appId", appListAdapter.getAppId(arg2 - 1));
				intent.putExtra("get_return", 0);
				intent.putExtra("appForRegId", 0);
				intent.putExtra("rImei", 0);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			}
		});
	}

	@Override
	protected void onResume() {
		// ++httpSearCode;
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		super.onResume();
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
	protected void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForSearch");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForSearch");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 根据关键字搜索应用
	 */
	private void httpForSearch() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APP_APPNAME;
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", appName);
		params.put("type", Httpaddress.PHONE_TYPE);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					stopLoad();
					if (isDropDown) {
						getNearByData(response);
						applist_view.onRefreshComplete();
					} else {
						List<UserAppInfo> list = setAppList(response);
						if (list != null) {
							if (list.size() > 0) {
								for (int i = 0; i < list.size(); i++) {
									appListAdapter.addUserAppInfo(list.get(i));
								}
							}
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
				stopLoad();
				if (!isDropDown) {
					--currentPage;
				}
				ToastUtil.show(SearchOflistActivity.this, CHString.NETWORK_BUSY);
				applist_view.onRefreshComplete();
				applist_view.onLoadMoreComplete(false);
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForSearch");
	}

	private void getNearByData(String arg0) {
		ResultStringForNearestuser resultString = new ResultStringForNearestuser();
		resultString = binder.fromJson(arg0, ResultStringForNearestuser.class);
		if (resultString != null) {
			if (resultString.getObj().getList().size() > 0) {
				appsList = setAppList(arg0);
				appListAdapter = new SearchListAdapter(SearchOflistActivity.this, appsList);
				applist_view.setAdapter(appListAdapter);
				applist_view.setSelector(new ColorDrawable(Color.TRANSPARENT));
			} else {
				finish();
				overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
				ToastUtil.show(this, CHString.ERROR_SEARCH);
			}
		} else {
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			ToastUtil.show(this, CHString.ERROR_SEARCH);
		}
	}

	/**
	 * 获取搜索app列表
	 */
	public List<UserAppInfo> setAppList(String arg0) {
		ResultStringForSearch resultString = new ResultStringForSearch();
		resultString = binder.fromJson(arg0, ResultStringForSearch.class);
		// listSize = resultString.getObj().getTotal() / pageSize;
		if (resultString.getObj() != null) {
			total = resultString.getObj().getTotal();
		}
		return resultString.getObj().getList();
	}

	@Override
	public void onRefresh() {
		isDropDown = true;
		currentPage = 0;
		httpForSearch();
	}

	@Override
	public void onLoadMore() {
		++currentPage;
		isDropDown = false;
		int totalPage = total / pageSize;
		int pageY = total % pageSize;
		if (pageY != 0) {
			++totalPage;
		}
		--totalPage;
		if (currentPage <= totalPage) {
			httpForSearch();
		} else {
			applist_view.onLoadMoreComplete(false);
			ToastUtil.show(SearchOflistActivity.this, CHString.THE_AFTER_PAGE);
		}
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		applist_view.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		applist_view.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		onRefresh();
	}

}
