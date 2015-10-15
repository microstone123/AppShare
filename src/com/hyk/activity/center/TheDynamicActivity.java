package com.hyk.activity.center;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.baseadapter.AppDynamicAdapter;
import com.hyk.baseadapter.MyScrollHandler;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.center.DyResult;
import com.hyk.center.DynamicInfo;
import com.hyk.center.OtherDylist;
import com.hyk.dialog.SetDyPopupWindow;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultDynamic;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.user.StatCommentHandle;
import com.hyk.utils.ACache;
import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.ImageTools;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.DyPullListView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;

public class TheDynamicActivity extends FragmentActivity implements OnClickListener,
		DyPullListView.OnRefreshLoadingMoreListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;
	private ImageButton btn_back;
	private String newsId;
	private String uName, uSign, redId;
	private int currentPage = 1;
	private int pageSize = 15;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();

	// private String redId;
	private DyPullListView dy_list_view;
	private AppDynamicAdapter appDynamicAdapter;
	private List<OtherDylist> list = new ArrayList<OtherDylist>();
	private int pageCount;
	private boolean isReft = false;
	private ACache mAcache;
	private String dy_string;
	private SharedPreferencesHelper spqq;
	private Drawable dra;
	private MyScrollHandler scrollHandler;
	private String content, focusHim, focusMe;
	private int sendNum = 0;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;

	// private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;
	private SetDyPopupWindow setDyPopupWindow;
	private static final int SET_FACE = 10013;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				if (setDyPopupWindow.isShowing()) {
					setDyPopupWindow.dismiss();
				}
				break;
			case SUCC_NETWORK:
				startLoad();
				framelayout.setVisibility(View.GONE);
				if (setDyPopupWindow.isShowing()) {
					setDyPopupWindow.dismiss();
				}
				break;
			case 10002:
				getNearByData(dy_string);
				break;
			case 10003:
				if (StringUtils.isNotEmpty(redId)) {
					newsId = msg.getData().getString("newsId");
					relatShow(newsId);
				} else {
					ToastUtil.show(TheDynamicActivity.this, CHString.NOT_LOGIN);
				}
				break;
			case SET_FACE:
				content = String.valueOf(msg.obj);
				Message msg1 = StatCommentHandle.getStatCommentHandle().getHandler().obtainMessage();
				Bundle b1 = new Bundle();// 存放数据
				b1.putInt("redId", Integer.valueOf(redId));
				b1.putString("nickName", uName);
				b1.putString("content", content);
				msg1.setData(b1);
				msg1.what = 102;
				StatCommentHandle.getStatCommentHandle().getHandler().sendMessage(msg1);

				sendComment();
				if (setDyPopupWindow.isShowing()) {
					setDyPopupWindow.dismiss();
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.the_dynamic);
		AppManager.getAppManager().addActivity(this);
		uName = getIntent().getExtras().getString("userName");
		uSign = getIntent().getExtras().getString("sign_name");
		redId = getIntent().getExtras().getString("redId");
		// requestQueue = Volley.newRequestQueue(this);
		mAcache = ACache.get(this);
		setupViews();
		scrollHandler = new MyScrollHandler(null, dy_list_view, null);

		setDyPopupWindow = new SetDyPopupWindow(this, handler);

		setDyPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				Message msg1 = StatCommentHandle.getStatCommentHandle().getHandler().obtainMessage();
				msg1.what = 101;
				StatCommentHandle.getStatCommentHandle().getHandler().sendMessage(msg1);
			}
		});
	}

	@Override
	protected void onStop() {
		try {
			// requestQueue.cancelAll("httpFormydynamic");
			XmppApplication.getsInstance().cancelPendingRequests("httpFormydynamic");
			if (StatCommentHandle.getStatCommentHandle().getHandler() != null) {
				StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
				Message msg1 = new Message();
				msg1.what = 101;
				StatCommentHandle.getStatCommentHandle().getHandler().sendMessage(msg1);
			}
			StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// requestQueue.cancelAll("httpFormydynamic");
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpFormydynamic");
		} catch (Exception e) {
		}
	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());

		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		dy_list_view = (DyPullListView) findViewById(R.id.dy_list_viewq);

		dy_list_view.setOnRefreshListener(this);
		appDynamicAdapter = new AppDynamicAdapter(this, list, scrollHandler, handler, redId, redId);
		dy_list_view.setAdapter(appDynamicAdapter);

	}

	@Override
	protected void onResume() {
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		try {

			if (NetworkUtil.checkNetworkState(this)) {
				startLoad();
			} else {
				stopLoad();
				dy_string = mAcache.getAsString("dynamic_string");
				if (StringUtils.isNotEmpty(dy_string)) {
					Message msg = new Message();
					msg.what = 10002;
					handler.sendMessage(msg);
				}
			}
			spqq = new SharedPreferencesHelper(this, "loginInfo");

			String redId = spqq.getValue("redId");

			String myurl = spqq.getValue(redId + "_logo");
			if (!myurl.startsWith("http")) {
				if (hasSdcard()) {
					if (StringUtils.isNotEmpty(myurl)) {
						File file = new File(spqq.getValue(redId + "_logo"));
						if (file.exists()) {
							Bitmap bm = BitmapFactory.decodeFile(spqq.getValue(redId + "_logo"));
							dra = ImageTools.bitmapToDrawable(bm);
						}
					} else {
						dra = getResources().getDrawable(R.drawable.the_default);
					}
				} else {
					dra = getResources().getDrawable(R.drawable.the_default);
				}
			}

			focusHim = spqq.getValue("focushim");
			focusMe = spqq.getValue("focusme");
			dy_list_view.setDy(uName, uSign, focusHim, focusMe, dra, myurl);

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResume();
	}

	/*
	 * 判断sdcard是否被挂载
	 */
	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
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

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	private List<OtherDylist> getNearByData(String arg0) {
		ResultDynamic resultString = new ResultDynamic();
		resultString = binder.fromJson(arg0, ResultDynamic.class);
		mAcache.put("dynamic_string", arg0);
		List<OtherDylist> newList = new ArrayList<OtherDylist>();
		if (resultString != null) {
			List<DynamicInfo> todayList = new ArrayList<DynamicInfo>();
			List<DynamicInfo> yesdayList = new ArrayList<DynamicInfo>();
			List<OtherDylist> otherlist0 = new ArrayList<OtherDylist>();
			DyResult dyResult = new DyResult();
			dyResult = resultString.getObj();
			if (dyResult != null) {
				Integer total = dyResult.getTotal();
				int chushu = total / pageSize;
				int yushu = total % pageSize;
				if (yushu != 0) {
					++chushu;
				}
				pageCount = chushu;
				todayList = dyResult.getTodayList();
				yesdayList = dyResult.getYesterdayList();
				otherlist0 = dyResult.getOtherlist();
				if (todayList != null) {
					if (todayList.size() > 0) {
						OtherDylist todayotherDylist = new OtherDylist();
						todayotherDylist.setMakeTime("今天");
						todayotherDylist.setDayNewsList(todayList);
						newList.add(todayotherDylist);
					}
				}
				if (yesdayList != null) {
					if (yesdayList.size() > 0) {
						OtherDylist todayotherDylist = new OtherDylist();
						todayotherDylist.setMakeTime("昨天");
						todayotherDylist.setDayNewsList(yesdayList);
						newList.add(todayotherDylist);
					}
				}
				if (otherlist0 != null) {
					if (otherlist0.size() > 0) {
						newList.addAll(otherlist0);
					}
				}
			}
		} else {
			dy_list_view.onRefreshComplete();
			dy_list_view.onLoadMoreComplete(false);
			ToastUtil.show(this, CHString.NOT_MAN);
		}
		return newList;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRefresh() {
		isReft = false;
		currentPage = 1;
		httpFormydynamic();
	}

	@Override
	public void onLoadMore() {
		isReft = true;
		currentPage++;
		if (currentPage <= pageCount) {
			httpFormydynamic();
		} else {
			dy_list_view.onLoadMoreComplete(false);
			ToastUtil.show(this, CHString.THE_AFTER_PAGE);
		}

	}

	/**
	 * 发表评论
	 */
	private void sendComment() {
		if (StringUtils.isNotEmpty(content)) {
			String url = Httpaddress.IP_ADDRESS + Httpaddress.SEND_COMMENT;
			Map<String, String> params = new HashMap<String, String>();
			params.put("newsId", newsId);
			params.put("content", content);
			params.put("commenterId", redId);
			String httpUrl = HttpUtils.checkUrl(url, params);
			jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					ResultStringForLoadap resultString = new ResultStringForLoadap();
					resultString = binder.fromJson(response, ResultStringForLoadap.class);
					if (resultString != null) {
						if (resultString.getResult() != 0) {
							++sendNum;
							if (sendNum <= 4) {
								if (NetworkUtil.checkNetworkState(TheDynamicActivity.this)) {
									sendComment();
								} else {
									ToastUtil.show(TheDynamicActivity.this, CHString.ERROR_SETCOMMENT);
								}
							}
						}
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					if (NetworkUtil.checkNetworkState(TheDynamicActivity.this)) {
						sendComment();
					} else {
						ToastUtil.show(TheDynamicActivity.this, CHString.ERROR_SETCOMMENT);
					}
				}
			});
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "sendComment");
		} else {
			ToastUtil.show(this, CHString.ERR_D);
		}
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		dy_list_view.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		dy_list_view.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		onRefresh();
	}

	// private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
	// @Override
	// protected String doInBackground(String... params) {
	// theMark = ++httpCode;
	// String jsonString = AppShowHttp.httpFormydynamic(TheDynamicActivity.this,
	// currentPage, pageSize, redId);
	// if (StringUtils.isNotEmpty(jsonString)) {
	// list = getNearByData(jsonString);
	// if (!isReft) {
	// jsonString = mAcache.getAsString("dynamic_string");
	// list = getNearByData(jsonString);
	// }
	// } else {
	// if (!isReft) {
	// --currentPage;
	// }
	// }
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// super.onPostExecute(result);
	// try {
	// stopLoad();
	// if (!isReft) {
	// // list.addAll(newList);
	// appDynamicAdapter = new AppDynamicAdapter(TheDynamicActivity.this, list,
	// scrollHandler, handler,
	// redId, redId);
	// dy_list_view.setAdapter(appDynamicAdapter);
	// dy_list_view.onRefreshComplete();
	// } else {
	// for (int i = 0; i < list.size(); i++) {
	// appDynamicAdapter.addOtherDylist(list.get(i));
	// }
	// }
	// dy_list_view.onRefreshComplete();
	// dy_list_view.onLoadMoreComplete(false);
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

	private void httpFormydynamic() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.GET_MYDYNAMIC;
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", redId);
		params.put("comCurrentPage", "1");
		params.put("comPageSize", "30");
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);

		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				stopLoad();
				list = getNearByData(response);
				try {
					stopLoad();
					if (!isReft) {
						// list.addAll(newList);
						appDynamicAdapter = new AppDynamicAdapter(TheDynamicActivity.this, list, scrollHandler,
								handler, redId, redId);
						dy_list_view.setAdapter(appDynamicAdapter);
						dy_list_view.onRefreshComplete();
					} else {
						for (int i = 0; i < list.size(); i++) {
							appDynamicAdapter.addOtherDylist(list.get(i));
						}
					}
					dy_list_view.onRefreshComplete();
					dy_list_view.onLoadMoreComplete(false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ToastUtil.show(TheDynamicActivity.this, CHString.NETWORK_ERROR);
			}
		});
//		jsonObjectRequest.setTag("httpFormydynamic");
//		requestQueue.add(jsonObjectRequest);
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpFormydynamic");
	}

	public void relatShow(String newsId) {
		setDyPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		setDyPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setDyPopupWindow.showAtLocation(findViewById(R.id.root_relat), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 10,
				10); // 设置layout在PopupWindow中显示的位置

		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

		setDyPopupWindow.clearEdit();
	}

}