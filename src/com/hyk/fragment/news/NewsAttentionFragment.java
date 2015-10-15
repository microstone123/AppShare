package com.hyk.fragment.news;

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
import com.hyk.baseadapter.AttentionUserAdapter;
import com.hyk.http.Httpaddress;
import com.hyk.news.AttentionInfo;
import com.hyk.resultforjson.ResultStringForAttention;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.PullListView;
import com.hyk.xmpp.XmppApplication;

public class NewsAttentionFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener {
	private int currentPage = 1;
	private int pageSize = 15;
	private boolean isDropDown = true;
	private int total = 0;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private PullListView attent_listview;
	private AttentionUserAdapter appListAdapter;
	private ACache mAcache;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private String regId = "0";
	private List<AttentionInfo> attentionInfo;
	private SharedPreferencesHelper spqq;
	private int relation;
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.me_attention_xml, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		requestQueue = Volley.newRequestQueue(getActivity());
		mAcache = ACache.get(getActivity());
		spqq = new SharedPreferencesHelper(getActivity(), "loginInfo");
		if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
			regId = spqq.getValue("redId");
		}
		relation = 0;
		setupViews();
	}

	public void setupViews() {
		attent_listview = (PullListView) getActivity().findViewById(R.id.me_attent_listview);
		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.me_loading_relat);
		loading_img = (ImageView) getActivity().findViewById(R.id.me_loading_img);
		attent_listview.setOnRefreshListener(this);
		startLoad();
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		attent_listview.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		attent_listview.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		if (NetworkUtil.checkNetworkState(getActivity())) {
			onRefresh();
		} else {
			stopLoad();
			if (StringUtils.isNotEmpty(mAcache.getAsString("attention_data"))) {
				getAttentByData(mAcache.getAsString("attention_data"));
			}
		}
	}

	@Override
	public void onRefresh() {
		isDropDown = true;
		currentPage = 1;
		httpForFriend();
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
			httpForFriend();
		} else {
			ToastUtil.show(getActivity(), CHString.THE_AFTER_PAGE);
			attent_listview.onLoadMoreComplete(false);
		}
	}


	private void getAttentByData(String arg0) {
		try {
			ResultStringForAttention resultString = new ResultStringForAttention();
			resultString = binder.fromJson(arg0, ResultStringForAttention.class);
			if (resultString != null) {
				if(resultString.getObj().getApps()!=null){
					if (resultString.getObj().getApps().size() > 0) {
						mAcache.put("attention_data", arg0);
						attentionInfo = resultString.getObj().getApps();
						if(attentionInfo!=null){
							if(attentionInfo.size()>0){
								appListAdapter = new AttentionUserAdapter(getActivity(), attentionInfo, relation,regId);
								attent_listview.setAdapter(appListAdapter);
							}
						}
					}
				}
			} else {
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 获取附近app列表
	 */
	public List<AttentionInfo> setAppList(String arg0) {
		ResultStringForAttention resultString = new ResultStringForAttention();
		resultString = binder.fromJson(arg0, ResultStringForAttention.class);
		return resultString.getObj().getApps();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		XmppApplication.getsInstance().cancelPendingRequests("attention_data");
	}

	/**
	 * 好友正在使用软件
	 */
	private void httpForFriend() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_APPLIST;
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", regId);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		params.put("mark", relation + "");
		String httpUrl = HttpUtils.checkUrl(url, params);
//		requestQueue.cancelAll("attention_data");
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					stopLoad();
					if (isDropDown) {
						getAttentByData(response);
						attent_listview.onRefreshComplete();
					} else {
						List<AttentionInfo> list = setAppList(response);
						if (list != null) {
							if (list.size() > 0) {
								for (int i = 0; i < list.size(); i++) {
									appListAdapter.addAttentionInfo(list.get(i));
								}
							}
						}
					}
					attent_listview.onLoadMoreComplete(false);
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
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
				if (isDropDown) {
					String newsStr = mAcache.getAsString("attention_data");
					if (StringUtils.isNotEmpty(newsStr)) {
						getAttentByData(newsStr);
					}
					attent_listview.onRefreshComplete();
				} else {
				}
				attent_listview.onLoadMoreComplete(false);
			}
		});
		
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "attention_data");

	}

}
