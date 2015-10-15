package com.hyk.fragment.mycenter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyk.activity.NearByActivity;
import com.hyk.activity.R;
import com.hyk.baseadapter.AppDynamicAdapter;
import com.hyk.baseadapter.MyScrollHandler;
import com.hyk.center.DyResult;
import com.hyk.center.DynamicInfo;
import com.hyk.center.OtherDylist;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultDynamic;
import com.hyk.utils.ACache;
import com.hyk.utils.AsyncHttpUtil;
import com.hyk.utils.CHString;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.DragListView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @ClassName: RadarFragment
 * @Description: TODO(获取个人动态)
 * @author linhs
 * @date 2014-2-21 下午3:54:26
 */
public class DynamicFragment extends Fragment implements DragListView.OnRefreshLoadingMoreListener {

	private int currentPage = 1;
	private int pageSize = 15;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private String myRegId;
	private int httpCode = 0;
	private int theMark;
	private String redId;
	private DragListView dy_list_view;
	private AppDynamicAdapter appDynamicAdapter;
	private List<OtherDylist> list;
	private int pageCount;
	private boolean isReft = false;
	private ACache mAcache;
	private String dy_string;
	private MyScrollHandler scrollHandler;
	private SharedPreferencesHelper spqq;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1002:
				getNearByData(dy_string);
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.dynamic_frag, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mAcache = ACache.get(getActivity());
		redId = mAcache.getAsString("dy_regid");
		spqq = new SharedPreferencesHelper(getActivity(), "loginInfo");
		myRegId = spqq.getValue("redId");
		setupViews();
		httpCode = 0;
		dy_string = mAcache.getAsString("dynamic_string");
		if (StringUtils.isNotEmpty(dy_string)) {
			// Message msg = new Message();
			// msg.what = 1002;
			// handler.sendMessage(msg);
		} else {
			// httpFormydynamic();
		}
	}

	public void setupViews() {
		dy_list_view = (DragListView) getActivity().findViewById(R.id.dy_list_viewq);
		dy_list_view.setOnRefreshListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// AsyncTaskUtil asyncTaskUtil = new AsyncTaskUtil();
		// asyncTaskUtil.execute();
	}

	@Override
	public void onPause() {
		super.onPause();
		++httpCode;
	}

	/**
	 * 请求我的动态数据
	 */
	private void httpFormydynamic() {
		theMark = ++httpCode;
		String url = Httpaddress.IP_ADDRESS + Httpaddress.GET_MYDYNAMIC;
		RequestParams params = new RequestParams();
		params.put("regId", "8");
		params.put("comCurrentPage", "1");
		params.put("comPageSize", "15");
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		// AsyncHttpUtil.cancelRequests(getActivity());
		AsyncHttpUtil.getJson(url, params, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject arg0) { // 返回的是JSONObject，会调用这里
				if (theMark == httpCode) {
					getNearByData(arg0.toString());
				}
			};

			public void onFailure(Throwable arg0) {
				try {
					if (theMark == httpCode) {
						ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			};

			public void onFinish() {

			};
		});
	}

	private void getNearByData(String arg0) {
		ResultDynamic resultString = new ResultDynamic();
		resultString = binder.fromJson(arg0, ResultDynamic.class);
		if (resultString != null) {
			List<DynamicInfo> todayList = new ArrayList<DynamicInfo>();
			List<DynamicInfo> yesdayList = new ArrayList<DynamicInfo>();
			List<OtherDylist> otherlist0 = new ArrayList<OtherDylist>();
			List<OtherDylist> list = new ArrayList<OtherDylist>();
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
						list.add(todayotherDylist);
					}
				}
				if (yesdayList != null) {
					if (yesdayList.size() > 0) {
						OtherDylist todayotherDylist = new OtherDylist();
						todayotherDylist.setMakeTime("昨天");
						todayotherDylist.setDayNewsList(yesdayList);
						list.add(todayotherDylist);
					}
				}
				if (otherlist0 != null) {
					if (otherlist0.size() > 0) {
						list.addAll(otherlist0);
					}
				}
				if (!isReft) {
					appDynamicAdapter = new AppDynamicAdapter(getActivity(), list, scrollHandler, handler, redId,
							myRegId);
					dy_list_view.setAdapter(appDynamicAdapter);
				} else {
					for (int i = 0; i < list.size(); i++) {
						appDynamicAdapter.addOtherDylist(list.get(i));
					}
				}
			}

			if (currentPage == pageCount) {
				dy_list_view.setFinalPage();
			}
			dy_list_view.onLoadMoreComplete(false);
		} else {
			ToastUtil.show(getActivity(), CHString.NOT_MAN);
			((NearByActivity) getActivity()).iv_scan.stopRotate();
		}
	}

	// private void putData(List<OtherDylist> list){
	//
	// }

	@Override
	public void onLoadMore() {
		isReft = true;
		currentPage++;
		if (currentPage <= pageCount) {
			httpFormydynamic();
		} else {
			dy_list_view.setFinalPage();
		}
	}

	private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

		}

		@Override
		public void onPreExecute() {

		}
	}

}
