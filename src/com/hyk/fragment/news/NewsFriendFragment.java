package com.hyk.fragment.news;

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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.hyk.baseadapter.AppFriendDynamicAdapter;
import com.hyk.baseadapter.MyScrollHandler;
import com.hyk.center.DynamicInfo;
import com.hyk.dialog.SetDyPopupWindow;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForFrDy;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.user.StatCommentHandle;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.PullListView;
import com.hyk.xmpp.XmppApplication;

public class NewsFriendFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener {

	private int currentPage = 1;
	private int pageSize = 15;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	// private RelativeLayout relat_layout;
	// private String redId;
	private PullListView dy_list_view;
	private String redId;
	private ACache mAcache;
	private boolean isReft = false;
	public MyScrollHandler scrollHandler;
	private EditText msgText;
	private String newsId;
	private int setFace = 0;
	private SetDyPopupWindow setDyPopupWindow;
	private ImageButton biaoqingBtn;
	private int total = 0;
	private boolean isDropDown = true;
	private String content;
	private Button formclient_btsend;
	private String uName;
	private SharedPreferencesHelper spqq;
	private int sendNum = 0;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private AppFriendDynamicAdapter appFriendDynamicAdapter;
	private List<DynamicInfo> dynamicInfoList;
	private static final int SET_FACE = 10013;

	// private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10003:
				if (StringUtils.isNotEmpty(redId)) {
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
		return inflater.inflate(R.layout.friend_dy, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		StatCommentHandle.getStatCommentHandle().setFragment(this);
		mAcache = ACache.get(getActivity());
		// requestQueue = Volley.newRequestQueue(getActivity());
		spqq = new SharedPreferencesHelper(getActivity(), "loginInfo");
		if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
			redId = spqq.getValue("redId");
			uName = spqq.getValue("user_name");
		}
		setView();
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

	private void setView() {
		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.loading_relat);
		loading_img = (ImageView) getActivity().findViewById(R.id.loading_img);
		dy_list_view = (PullListView) getActivity().findViewById(R.id.f_dy_f);
		dy_list_view.setOnRefreshListener(this);
		scrollHandler = new MyScrollHandler(dy_list_view, null, null);

	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		dy_list_view.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
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

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		dy_list_view.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		if (NetworkUtil.checkNetworkState(getActivity())) {
			onRefresh();
		} else {
			stopLoad();
			if (StringUtils.isNotEmpty(mAcache.getAsString("friend_data_string"))) {
				getAttentByData(mAcache.getAsString("friend_data_string"));
			}
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
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "sendComment");
		} else {
			ToastUtil.show(getActivity(), CHString.ERR_D);
		}
	}

	private void getAttentByData(String arg0) {
		ResultStringForFrDy resultString = new ResultStringForFrDy();
		Log.e("httpForFrienddynamic", arg0);
		resultString = binder.fromJson(arg0, ResultStringForFrDy.class);
		if (resultString != null) {
			if (resultString.getObj().getNewslist().size() > 0) {
				mAcache.put("friend_data_string", arg0);
				dynamicInfoList = resultString.getObj().getNewslist();
				if (dynamicInfoList != null) {
					if (dynamicInfoList.size() > 0) {
						appFriendDynamicAdapter = new AppFriendDynamicAdapter(getActivity(), dynamicInfoList,
								scrollHandler, handler, redId);
						dy_list_view.setAdapter(appFriendDynamicAdapter);
					}
				}
				Log.e("httpForFrienddynamic", resultString.getObj().getNewslist().size() + "");
			} else {
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
			}
		} else {
			ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
		}
	}

	/**
	 * 获取附近app列表
	 */
	public List<DynamicInfo> setAppList(String arg0) {
		ResultStringForFrDy resultString = new ResultStringForFrDy();
		resultString = binder.fromJson(arg0, ResultStringForFrDy.class);
		return resultString.getObj().getNewslist();
	}

	@Override
	public void onRefresh() {
		isDropDown = true;
		currentPage = 1;
		httpForFrienddynamic();
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
			httpForFrienddynamic();
		} else {
			ToastUtil.show(getActivity(), CHString.THE_AFTER_PAGE);
			dy_list_view.onLoadMoreComplete(false);
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
			XmppApplication.getsInstance().cancelPendingRequests("httpFormydynamic");
			XmppApplication.getsInstance().cancelPendingRequests("sendComment");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		startLoad();
	}

	/**
	 * 获取用户交际圈动态
	 */
	private void httpForFrienddynamic() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USERNEWS_OTHER;
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
				try {
					stopLoad();
					dynamicInfoList = getData(response);
					if (dynamicInfoList != null) {
						if (isDropDown) {

							if (dynamicInfoList != null) {
								if (dynamicInfoList.size() > 0) {
									appFriendDynamicAdapter = new AppFriendDynamicAdapter(getActivity(),
											dynamicInfoList, scrollHandler, handler, redId);
									dy_list_view.setAdapter(appFriendDynamicAdapter);
								}
							}
						} else {
							if (dynamicInfoList != null) {
								if (dynamicInfoList.size() > 0) {
									for (int i = 0; i < dynamicInfoList.size(); i++) {
										appFriendDynamicAdapter.addDynamicInfo(dynamicInfoList.get(i));
									}
								}
							}
						}
					} else {
						if (isDropDown) {
							if (StringUtils.isNotEmpty(mAcache.getAsString("friend_data_string"))) {
								dynamicInfoList = getData(mAcache.getAsString("friend_data_string"));
								if (dynamicInfoList != null) {
									if (dynamicInfoList.size() > 0) {
										appFriendDynamicAdapter = new AppFriendDynamicAdapter(getActivity(),
												dynamicInfoList, scrollHandler, handler, redId);
										dy_list_view.setAdapter(appFriendDynamicAdapter);
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				stopLoad();
				if (!isReft) {
					--currentPage;
				} else {
					String str = mAcache.getAsString("friend_data_string");
					if (StringUtils.isNotEmpty(str)) {
						dynamicInfoList = getData(str);
						if (dynamicInfoList != null) {
							if (dynamicInfoList.size() > 0) {
								appFriendDynamicAdapter = new AppFriendDynamicAdapter(getActivity(), dynamicInfoList,
										scrollHandler, handler, redId);
								dy_list_view.setAdapter(appFriendDynamicAdapter);
							}
						}
					}
				}
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpFormydynamic");
	}

	public List<DynamicInfo> getData(String str) {
		List<DynamicInfo> list = new ArrayList<DynamicInfo>();
		ResultStringForFrDy resultString = new ResultStringForFrDy();
		resultString = binder.fromJson(str, ResultStringForFrDy.class);
		if (resultString != null) {
			if (resultString.getObj() != null) {
				list = resultString.getObj().getNewslist();
				if (list.size() > 0) {
					mAcache.put("friend_data_string", str);
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
		return list;
	}

}
