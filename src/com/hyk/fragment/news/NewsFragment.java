package com.hyk.fragment.news;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.AnimationUtil;
import com.hyk.activity.NewsActivity;
import com.hyk.activity.R;
import com.hyk.activity.news.ChatActivity;
import com.hyk.baseadapter.NewsMessageAdapter;
import com.hyk.dialog.LoginDiao;
import com.hyk.http.Httpaddress;
import com.hyk.news.NewsForInfo;
import com.hyk.resultforjson.ResultStringForNewForInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.ToastUtil;
import com.hyk.view.PullListView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppConnection;
import com.hyk.xmpp.listener.MessageListener;

/**
 * @ClassName: AppCommentFragment
 * @Description: TODO(登陆)
 * @author linhs
 * @date 2014-2-26 下午2:59:21
 */
public class NewsFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener {

	private PullListView news_listview;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private int currentPage = 1;
	private int pageSize = 30;
	private boolean isDropDown = true;
	private int total = 0;
	private String regId = "0";
	private NewsMessageAdapter appListAdapter;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private List<NewsForInfo> newsForInfoList = new ArrayList<NewsForInfo>();
	private ACache mAcache;
	private boolean isFirst = true;
	private static final int UPDATA_NEWS = 10021;
	private static final int START_LOAD_MARK = 100121;
	private LoginDiao loginDiao;
	private StringRequest jsonObjectRequest;

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATA_NEWS:
				try {
					appListAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			case 100235:
				// int[] data = (int[]) msg.obj;
				// Log.e("下载", data[2] + "  " + data[1] + "  " + data[0]);
				break;
			case START_LOAD_MARK:
				if (loginDiao.isShowing()) {
					loginDiao.dismiss();
				}
				// startLoad();
				break;
			}
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_cont, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mAcache = ACache.get(getActivity());
		regId = ((NewsActivity) getActivity()).redId;
		// requestQueue = Volley.newRequestQueue(getActivity());

		setupViews();
		MessageListener.handler = handler;
		startLoad();
		Log.e("NewsFragment", XmppConnection.isConnected() + "");
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		try {
			if (!isFirst) {
				startLoad();
			}
			isFirst = false;
			MessageListener.onChat = 0;
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResume();
	}

	public void setupViews() {
		loading_img = (ImageView) getActivity().findViewById(R.id.loading_img);
		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.loading_relat);
		news_listview = (PullListView) getActivity().findViewById(R.id.news_listview);
		news_listview.setOnRefreshListener(this);
		appListAdapter = new NewsMessageAdapter(getActivity(), newsForInfoList, regId);
		news_listview.setAdapter(appListAdapter);
		news_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewSetClick.getAlpha(arg1, getActivity());
				Intent intent = new Intent();
				intent.putExtra("user_name", appListAdapter.getData(arg2 - 1).getUserName());
				intent.putExtra("user_img", appListAdapter.getData(arg2 - 1).getHeadPic());
				intent.putExtra("user_regid", String.valueOf(appListAdapter.getData(arg2 - 1).getRegId()));
				intent.setClass(getActivity(), ChatActivity.class);
				AnimationUtil.setLayout(R.anim.push_left_in, R.anim.push_left_out);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onPause() {
		isDropDown = false;
		MessageListener.onChat = 1;
		super.onPause();
	}

	@Override
	public void onRefresh() {
		isDropDown = true;
		currentPage = 1;
		httpForMessagelist();
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
			httpForMessagelist();
		} else {
			currentPage--;
			// ToastUtil.show(getActivity(), CHString.THE_AFTER_PAGE);
			news_listview.onLoadMoreComplete(false);
		}
	}

	private void getNewsByData(String arg0) {
		ResultStringForNewForInfo resultString = new ResultStringForNewForInfo();
		resultString = binder.fromJson(arg0, ResultStringForNewForInfo.class);
		if (resultString != null) {
			if (resultString.getObj().getList().size() > 0) {
				newsForInfoList = setAppList(arg0);
				appListAdapter = new NewsMessageAdapter(getActivity(), newsForInfoList, regId);
				news_listview.setAdapter(appListAdapter);
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
	public List<NewsForInfo> setAppList(String arg0) {
		ResultStringForNewForInfo resultString = new ResultStringForNewForInfo();
		resultString = binder.fromJson(arg0, ResultStringForNewForInfo.class);
		if (resultString != null) {
			if (resultString.getObj().getList() != null) {
				total = resultString.getObj().getTotal();
				if (isDropDown) {
					mAcache.getAsString("news_mesage");
				}
			}
			return resultString.getObj().getList();
		} else {
			return null;
		}
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		// news_listview.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		// news_listview.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		if (NetworkUtil.checkNetworkState(getActivity())) {
			onRefresh();
		} else {
			stopLoad();
			String response = mAcache.getAsString("news_response");
			if (StringUtils.isNotEmpty(response)) {
				getNewsByData(response);
			}
		}
	}

	private void httpForMessagelist() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.NEWS_MESSAGELIST;
		// String url = "http://192.168.0.32:8080/" +
		// Httpaddress.NEWS_MESSAGELIST;
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);
		Log.i("httpForMessagelist ", httpUrl);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				stopLoad();
				newsForInfoList = setAppList(response);
				try {
					stopLoad();
					if (newsForInfoList != null) {
						if (isDropDown) {
							appListAdapter = new NewsMessageAdapter(getActivity(), newsForInfoList, regId);
							news_listview.setAdapter(appListAdapter);

							mAcache.put("news_response", response);
						} else {
							for (int i = 0; i < newsForInfoList.size(); i++) {
								appListAdapter.addNewsForInfo(newsForInfoList.get(i));
							}
						}
					}
					news_listview.onRefreshComplete();
					news_listview.onLoadMoreComplete(false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				stopLoad();
				ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
				if (!isDropDown) {
					currentPage--;
				} else {
					String response = mAcache.getAsString("news_response");
					if (StringUtils.isNotEmpty(response)) {
						newsForInfoList = setAppList(response);
						appListAdapter = new NewsMessageAdapter(getActivity(), newsForInfoList, regId);
						news_listview.setAdapter(appListAdapter);
					}
				}
				news_listview.onRefreshComplete();
				news_listview.onLoadMoreComplete(false);
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForMessagelist");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForMessagelist");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		stopLoad();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForMessagelist");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
