package com.hyk.fragment.news;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.hyk.baseadapter.FansUserAdapter;
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

public class NewsFansFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener {
	private int currentPage = 1;
	private int pageSize = 15;
	private boolean isDropDown = true;
	private int total = 0;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private PullListView attent_listview;
	private FansUserAdapter appListAdapter;
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

		// TextView viewhello = (TextView)
		// view.getActivity().findViewById(R.id.tv_hello);
		// viewhello.setText(maplist.get("userid")+"time");
		return inflater.inflate(R.layout.attention_xml, container, false);

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
		relation = 1;
		setupViews();
	}

	public void setupViews() {
		attent_listview = (PullListView) getActivity().findViewById(R.id.attent_listview);
		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.loading_relat_me);
		loading_img = (ImageView) getActivity().findViewById(R.id.loading_img_me);
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
			if (StringUtils.isNotEmpty(mAcache.getAsString("fans_data"))) {
				attentionInfo = getAttentByData(mAcache.getAsString("fans_data"));
				if(attentionInfo!=null){
					if(attentionInfo.size()>0){
						appListAdapter = new FansUserAdapter(getActivity(), attentionInfo, relation,regId);
						attent_listview.setAdapter(appListAdapter);
					}
				}
			}
		}
	}

	@Override
	public void onRefresh() {
		isDropDown = true;
		currentPage = 1;
		// httpForFriend();
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

	private List<AttentionInfo> getAttentByData(String arg0) {
		ResultStringForAttention resultString = new ResultStringForAttention();
		resultString = binder.fromJson(arg0, ResultStringForAttention.class);
		if (resultString != null) {
			if (resultString.getObj().getApps().size() > 0) {
				mAcache.put("fans_data", arg0);
				attentionInfo = resultString.getObj().getApps();
			} else {
				return null;
			}
		} else {
			return null;
		}
		return attentionInfo;
	}

	/**
	 * 获取附近app列表
	 */
	public List<AttentionInfo> setAppList(String arg0) {
		ResultStringForAttention resultString = new ResultStringForAttention();
		resultString = binder.fromJson(arg0, ResultStringForAttention.class);
		return resultString.getObj().getApps();
	}

//	private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
//		@Override
//		protected String doInBackground(String... params) {
//			theMark = ++httpCode;
//			String jsonString = AppShowHttp.httpForFriend(getActivity(), currentPage, pageSize, regId, relation);
//			if (StringUtils.isNotEmpty(jsonString)) {
//				attentionInfo = getAttentByData(jsonString);
//				if (isDropDown) {
//					jsonString = mAcache.getAsString("fans_data");
//					attentionInfo = getAttentByData(jsonString);
//				}
//			} else {
//				if (!isDropDown) {
//					--currentPage;
//				}
//			}
//
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//			try {
//				if (theMark == httpCode) {
//					stopLoad();
//					if (attentionInfo != null) {
//						if (attentionInfo.size() > 0) {
//							if (isDropDown) {
//								appListAdapter = new FansUserAdapter(getActivity(), attentionInfo, relation);
//								attent_listview.setAdapter(appListAdapter);
//							} else {
//								if (attentionInfo != null) {
//									if (attentionInfo.size() > 0) {
//										for (int i = 0; i < attentionInfo.size(); i++) {
//											appListAdapter.addAttentionInfo(attentionInfo.get(i));
//										}
//									}
//								}
//							}
//						}
//					} else {
//						ToastUtil.show(getActivity(), CHString.NETWORK_ERROR);
//					}
//					attent_listview.onRefreshComplete();
//					attent_listview.onLoadMoreComplete(false);
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//
//		@Override
//		public void onPreExecute() {
//
//		}
//	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e("onResume", "fa onResume");
	}

	@Override
	public void onStop() {
		super.onStop();
		XmppApplication.getsInstance().cancelPendingRequests("httpForFriend");
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
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					stopLoad();
					if (isDropDown) {
						attentionInfo = getAttentByData(response);
						if(attentionInfo.size()>0){
							appListAdapter = new FansUserAdapter(getActivity(), attentionInfo, relation,regId);
							attent_listview.setAdapter(appListAdapter);
						}
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
					attent_listview.onRefreshComplete();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				stopLoad();
				if (currentPage > 0) {
					--currentPage;
				}
				ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
				if (isDropDown) {
					String newsStr = mAcache.getAsString("attention_data");
					if (StringUtils.isNotEmpty(newsStr)) {
						attentionInfo = getAttentByData(newsStr);
						if(attentionInfo!=null){
							if(attentionInfo.size()>0){
								appListAdapter = new FansUserAdapter(getActivity(), attentionInfo, relation,regId);
								attent_listview.setAdapter(appListAdapter);
							}
						}
					}
					attent_listview.onRefreshComplete();
				} else {
				}
				attent_listview.onLoadMoreComplete(false);
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForFriend");
	}

}
