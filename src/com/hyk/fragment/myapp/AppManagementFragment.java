package com.hyk.fragment.myapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.app.UpdataApp;
import com.hyk.app.UpdataAppObject;
import com.hyk.baseadapter.AppManagementAdapter;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForUpdataList;
import com.hyk.user.UserInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.ToastUtil;
import com.hyk.view.PullListView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;

/**
 * @ClassName: AppCommentActivity
 * @Description: TODO(应用管理界面)
 * @author linhs
 * @date 2014-3-6 下午2:02:44
 */
public class AppManagementFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener,
		OnClickListener {

	private PullListView recen_listview;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private List<UpdataApp> updataApplist = new ArrayList<UpdataApp>();
	private AppManagementAdapter appUpdataAdapter;
	public int hhhhh = 9;
	public RelativeLayout linear_comm;
	private ACache mAcache;
	private static final int UPDATA_APK_END = 9000041;
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private int pageSize = 30;
	private int currentPage = 1;
	private boolean isDropDown;
	private AnimationDrawable animationDrawable;
	private Button set_update;
	private String TotalNum = "0MB";
	private static final int DOWN_TILE = 1010;
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATA_APK_END:
				String appName = msg.getData().getString("appName");
				for (int i = 0; i < updataApplist.size(); i++) {
					if (appName.equals(updataApplist.get(i).getAppName())) {
						updataApplist.remove(i);
						break;
					}
				}
				appUpdataAdapter = new AppManagementAdapter(getActivity(), updataApplist, handler);
				recen_listview.setAdapter(appUpdataAdapter);

				break;
			case DOWN_TILE:
				// one_key_progressBar
				Log.e("DOWN_TILE", DOWN_TILE + "");
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// TextView viewhello = (TextView)
		// view.getActivity().findViewById(R.id.tv_hello);
		// viewhello.setText(maplist.get("userid")+"time");
		return inflater.inflate(R.layout.comment_app, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		requestQueue = Volley.newRequestQueue(getActivity());
		setupViews();
	}

	public void setupViews() {
		set_update = (Button) getActivity().findViewById(R.id.set_update);
		linear_comm = (RelativeLayout) getActivity().findViewById(R.id.linear_comm);
		recen_listview = (PullListView) getActivity().findViewById(R.id.recen_listview_comm);
		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.loading_relat_comm);
		loading_img = (ImageView) getActivity().findViewById(R.id.loading_img_comm);
		recen_listview.setOnRefreshListener(this);
		recen_listview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		set_update.setOnClickListener(this);
		mAcache = ACache.get(getActivity());
		startLoad();
	}

	private void getData(String jsonString) {
		updataApplist = getList(jsonString);
		if (updataApplist == null) {
			updataApplist = new ArrayList<UpdataApp>();
		}
		if (updataApplist.size() > 0) {
			mAcache.put("manage_updata_string", jsonString);
		}
		appUpdataAdapter = new AppManagementAdapter(getActivity(), updataApplist, handler);
		recen_listview.setAdapter(appUpdataAdapter);
	}

	private List<UpdataApp> getList(String jsonString) {
		ResultStringForUpdataList resultString = new ResultStringForUpdataList();
		resultString = binder.fromJson(jsonString, ResultStringForUpdataList.class);
		UpdataAppObject appHotList = new UpdataAppObject();
		if (resultString != null) {
			appHotList = resultString.getObj();
			TotalNum = String.valueOf(appHotList.getSize());
			// set_update.setText(CHString.ONEKEY_UPDATA + "(" + TotalNum +
			// "MB)");
		} else {
			return null;
		}
		return appHotList.getList();
	}


	@Override
	public void onRefresh() {
		// String jsonString = mAcache.getAsString("updata_string");
		// getData(jsonString);
		currentPage = 1;
		isDropDown = true;
		httpForAppUpdata();
	}

	@Override
	public void onLoadMore() {
		recen_listview.onLoadMoreComplete(false);
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		linear_comm.setVisibility(View.VISIBLE);
		// recen_listview.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		// recen_listview.setVisibility(View.GONE);
		linear_comm.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		onRefresh();
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, getActivity());
		switch (v.getId()) {
		case R.id.set_update:
			switch (appUpdataAdapter.getUpdata()) {
			case 0: // 初始状态
				set_update.setText(CHString.APP_PAUSE + " (" + TotalNum + "MB)");
				appUpdataAdapter.setUpdata(1);
				XmppApplication.downloadsChangeObserver.getUpdate().setStatus(1);
				break;
			case 1: // 开始
				set_update.setText(CHString.APP_CONTINUE + " (" + TotalNum + "MB)");
				appUpdataAdapter.setUpdata(2);
				XmppApplication.downloadsChangeObserver.getUpdate().setStatus(2);
				break;
			case 2: // 暂停
				set_update.setText(CHString.APP_PAUSE + " (" + TotalNum + "MB)");
				appUpdataAdapter.setUpdata(3);
				XmppApplication.downloadsChangeObserver.getUpdate().setStatus(3);
				break;
			case 3: // 暂停
				set_update.setText(CHString.APP_PAUSE + " (" + TotalNum + "MB)");
				appUpdataAdapter.setUpdata(1);
				XmppApplication.downloadsChangeObserver.getUpdate().setStatus(1);
				break;
			default:
				break;
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		try {
			appUpdataAdapter.notifyDataSetChanged();
			switch (XmppApplication.downloadsChangeObserver.getUpdate().getStatus()) {
			case 0:
				set_update.setText(CHString.ONEKEY_UPDATA + " (" + TotalNum + "MB)");
				break;
			case 1:
				set_update.setText(CHString.APP_PAUSE + " (" + TotalNum + "MB)");
				break;
			case 2:
				set_update.setText(CHString.APP_CONTINUE + " (" + TotalNum + "MB)");
				break;
			case 3:
				set_update.setText(CHString.APP_CONTINUE + " (" + TotalNum + "MB)");
				break;

			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResume();
	}

	/**
	 * 请求更新数据
	 */
	private void httpForAppUpdata() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APP_UPDATE;
		Map<String, String> params = new HashMap<String, String>();
		params.put("imei", UserInfo.getUserInfo().getIMEI());
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					stopLoad();
					if (isDropDown) {
						getData(response);
					} else {
						List<UpdataApp> list = getList(response);
						if (list != null) {
							if (list.size() > 0) {
								for (int i = 0; i < list.size(); i++) {
									appUpdataAdapter.addData(list.get(i));
								}
							}
						}
					}
					recen_listview.onRefreshComplete();
					recen_listview.onLoadMoreComplete(false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				try {
					stopLoad();
					if (isDropDown) {
						String jsonString = mAcache.getAsString("manage_updata_string");
						if (StringUtils.isNotEmpty(jsonString)) {
							getData(jsonString);
						}
					}
					if (currentPage > 0) {
						--currentPage;
					}
					ToastUtil.show(getActivity(), CHString.NETWORK_BUSY);
					recen_listview.onRefreshComplete();
					recen_listview.onLoadMoreComplete(false);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForAppUpdata");
	}

	@Override
	public void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForAppUpdata");
		} catch (Exception e) {
		}
	}

}
