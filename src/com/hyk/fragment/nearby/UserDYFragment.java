package com.hyk.fragment.nearby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.hyk.center.DyResult;
import com.hyk.center.DynamicInfo;
import com.hyk.center.OtherDylist;
import com.hyk.dialog.SetDyPopupWindow;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultDynamic;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.user.StatCommentHandle;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.FragmentManager;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.PullListView;
import com.hyk.xmpp.XmppApplication;

public class UserDYFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener {

	private int currentPage = 1;
	private int pageSize = 5;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private PullListView dy_list_view;
	private AppDynamicAdapter appDynamicAdapter;
	private List<OtherDylist> list = new ArrayList<OtherDylist>();
	private int pageCount;
	private String redId;
	private String commentId;
	private ACache mAcache;
	private boolean isReft = true;
	public MyScrollHandler scrollHandler;
	private String newsId;
	private String content;
	private SharedPreferencesHelper spqq;
	private int sendNum = 0;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	public RelativeLayout relat_layout;
	private SetDyPopupWindow setDyPopupWindow;
	private static final int SET_FACE = 10013;
	// private RequestQueue requestQueue;
	private String uName;
	private StringRequest jsonObjectRequest;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10003:
				if (StringUtils.isNotEmpty(commentId)) {
					newsId = msg.getData().getString("newsId");
					relatShow(newsId);
				} else {
					ToastUtil.show(getActivity(), CHString.NOT_LOGIN);
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
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.user_dy, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentManager.getAppManager().addFragment(this);
		StatCommentHandle.getStatCommentHandle().setFragment(this);
		spqq = new SharedPreferencesHelper(getActivity(), "loginInfo");
		uName = spqq.getValue("user_name");
		commentId = spqq.getValue("redId");
		mAcache = ACache.get(getActivity());
		redId = mAcache.getAsString("dy_regid");

		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.loading_relat_dy);
		loading_img = (ImageView) getActivity().findViewById(R.id.loading_img_dy);
		dy_list_view = (PullListView) getActivity().findViewById(R.id.u_dy_f);
		dy_list_view.setOnRefreshListener(this);
		scrollHandler = new MyScrollHandler(dy_list_view, null, null);
		isReft = false;
		currentPage = 1;
		startLoad();

		setDyPopupWindow = new SetDyPopupWindow(getActivity(), handler);

		setDyPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				Message msg1 = StatCommentHandle.getStatCommentHandle().getHandler().obtainMessage();
				msg1.what = 101;
				StatCommentHandle.getStatCommentHandle().getHandler().sendMessage(msg1);
			}
		});
	}

	private List<OtherDylist> getNearByData(String arg0) {
		ResultDynamic resultString = new ResultDynamic();
		resultString = binder.fromJson(arg0, ResultDynamic.class);
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

				if (isReft) {
					mAcache.put(redId + "_my_dyna_string", arg0);
				}

			}
		} else {
			ToastUtil.show(getActivity(), CHString.NOT_MAN);
		}
		return newList;
	}

	@Override
	public void onRefresh() {
		isReft = true;
		currentPage = 1;
		httpFormydynamic();
	}

	@Override
	public void onLoadMore() {
		isReft = false;
		currentPage++;
		if (currentPage <= pageCount) {
			httpFormydynamic();
		} else {
			dy_list_view.onLoadMoreComplete(false);
			ToastUtil.show(getActivity(), CHString.THE_AFTER_PAGE);
		}
	}

	public void relatShow(String newsId) {
		setDyPopupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		setDyPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		setDyPopupWindow.showAtLocation(getActivity().findViewById(R.id.root_relat), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 10, 10); // 设置layout在PopupWindow中显示的位置

		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

		setDyPopupWindow.clearEdit();
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
			params.put("commenterId", commentId);
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
								if (NetworkUtil.checkNetworkState(getActivity())) {
									sendComment();
								} else {
									ToastUtil.show(getActivity(), CHString.ERROR_SETCOMMENT);
								}
							}
						}
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					if (NetworkUtil.checkNetworkState(getActivity())) {
						sendComment();
					} else {
						ToastUtil.show(getActivity(), CHString.ERROR_SETCOMMENT);
					}
				}
			});
			// jsonObjectRequest.setTag("sendComment");
			// requestQueue.add(jsonObjectRequest);
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "sendComment");
		} else {
			ToastUtil.show(getActivity(), CHString.ERR_D);
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
		dy_list_view.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		onRefresh();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		ToastUtil.show(getActivity(), "onKeyDown");

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			return true;
		} else {
			return true;
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (StatCommentHandle.getStatCommentHandle().getHandler() != null) {
			StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
			appDynamicAdapter.notifyDataSetChanged();
		}
		// requestQueue.cancelAll("httpFormydynamic");
		// requestQueue.cancelAll("sendComment");
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpFormydynamic");
			XmppApplication.getsInstance().cancelPendingRequests("sendComment");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpFormydynamic");
			XmppApplication.getsInstance().cancelPendingRequests("sendComment");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 请求我的动态数据
	 */
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
				if (list != null) {
					if (list.size() > 0) {
						if (isReft) {
							appDynamicAdapter = new AppDynamicAdapter(getActivity(), list, scrollHandler, handler,
									redId, commentId);
							dy_list_view.setAdapter(appDynamicAdapter);
						} else {
							for (int i = 0; i < list.size(); i++) {
								appDynamicAdapter.addOtherDylist(list.get(i));
								dy_list_view.onLoadMoreComplete(false);
							}
						}
						dy_list_view.onRefreshComplete();
					}
				}
				dy_list_view.onRefreshComplete();
				dy_list_view.onLoadMoreComplete(false);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				stopLoad();
				if (!isReft) {
					--currentPage;
				} else {
					String jsonString = mAcache.getAsString(redId + "_my_dyna_string");
					if (StringUtils.isNotEmpty(jsonString)) {
						list = getNearByData(jsonString);
						appDynamicAdapter = new AppDynamicAdapter(getActivity(), list, scrollHandler, handler, redId,
								commentId);
						dy_list_view.setAdapter(appDynamicAdapter);
					}
				}
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
			}
		});
//		jsonObjectRequest.setTag("httpFormydynamic");
//		requestQueue.add(jsonObjectRequest);
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpFormydynamic");
	}
}
