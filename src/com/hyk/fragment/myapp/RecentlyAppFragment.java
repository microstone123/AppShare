package com.hyk.fragment.myapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hyk.activity.R;
import com.hyk.app.MyAppComment;
import com.hyk.baseadapter.MoreAppListAdapter;
import com.hyk.broadcast.InstalledReceiver;
import com.hyk.myapp.GetPhoneApp;
import com.hyk.myapp.LocationApp;
import com.hyk.myapp.MyappDatabaseUtil;
import com.hyk.myapp.Programe;
import com.hyk.utils.DateUtils;
import com.hyk.view.PullListView;
import com.hyk.view.ViewSetClick;

/**
 * @ClassName: RecentlyAppActivity
 * @Description: TODO(最近使用应用界面)
 * @author linhs
 * @date 2014-3-6 下午2:01:14
 */
public class RecentlyAppFragment extends Fragment implements PullListView.OnRefreshLoadingMoreListener {

	@SuppressLint("HandlerLeak")
	private static final int UNINSTALL_APK = 900001;
	private static final int SUCCESS_UNINSTALL = 900002;
	private static final int UPDATA_APP = 1002;
	private static final int GET_APP = 1003;
	private String packName;
	public String appName;
	private InstalledReceiver installedReceiver = null;
	private PullListView recen_listview;
	private MoreAppListAdapter appGridAdapter;
	private MyappDatabaseUtil myappDatabaseUtil;
	private static final int THE_RECENT_APP = 0;
	private static final int THE_MY_APP = 1;
	private static final int THE_SYS_APP = 2;
	private List<Programe> prList;
	private List<LocationApp> recentlyApp = new ArrayList<LocationApp>();
	private List<LocationApp> oldRecentlyApp = new ArrayList<LocationApp>();
	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	// private boolean isf

	private boolean isLongClick = true;

	@SuppressLint("HandlerLeak")
	public Handler handler = new Handler() {
		@SuppressLint("NewApi")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UNINSTALL_APK:
				packName = msg.getData().getString("packName");
				appName = msg.getData().getString("appName");
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				intent.setAction("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:" + packName));
				startActivityForResult(intent, 200);
				break;

			case SUCCESS_UNINSTALL:

				if (StringUtils.isNotEmpty(appName)) {
					myappDatabaseUtil.delete(appName);
					appGridAdapter.setData(myappDatabaseUtil.queryForTime(THE_RECENT_APP));
				}

				break;

			case UPDATA_APP:

				appGridAdapter.setData(myappDatabaseUtil.queryForTime(THE_RECENT_APP));
				break;
			case 0:
				appGridAdapter.notifyDataSetChanged();
				break;
			case GET_APP:
				recentlyApp = myappDatabaseUtil.queryForTime(THE_RECENT_APP);
				appGridAdapter = new MoreAppListAdapter(getActivity(), recentlyApp, handler);
				recen_listview.setAdapter(appGridAdapter);
				break;
			}
		}

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		// TextView viewhello = (TextView) view.findViewById(R.id.tv_hello);
		// viewhello.setText(maplist.get("userid")+"time");
		return inflater.inflate(R.layout.recen_app, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		myappDatabaseUtil = new MyappDatabaseUtil(getActivity());
		installedReceiver = new InstalledReceiver(handler);
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addDataScheme("package");
		getActivity().registerReceiver(installedReceiver, filter);

		recen_listview = (PullListView) getActivity().findViewById(R.id.recen_listview);
		recen_listview.setSelector(R.drawable.hide_listview_yellow_selector);
		recen_listview.setOnRefreshListener(this);

		loading_relat = (RelativeLayout) getActivity().findViewById(R.id.recen_loading_relat);
		loading_img = (ImageView) getActivity().findViewById(R.id.recen_loading_img);

		startLoad();

		recen_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				ViewSetClick.getAlpha(arg1, getActivity());
				myappDatabaseUtil.updateTime(appGridAdapter.getData(arg2 - 1).getName(),
						DateUtils.getSysDate("yyyy-MM-dd HH:mm:ss"));
				UpdataGridAdapter();
				MyAppComment.startApk(getActivity(), appGridAdapter.getData(arg2 - 1).getPackName());
			}
		});
		appGridAdapter = new MoreAppListAdapter(getActivity(), recentlyApp, handler);
		recen_listview.setAdapter(appGridAdapter);
	}

	public void UpdataGridAdapter() {
		Message msg = new Message();
		msg.what = UPDATA_APP;
		handler.sendMessage(msg);
	}

	public void showAsyncTask() {
		loading_relat.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		AsyncTaskUtil asyncTaskUtil = new AsyncTaskUtil();
		asyncTaskUtil.execute();
	}

	private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			oldRecentlyApp = GetPhoneApp.getRunningProcess(getActivity());
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			appGridAdapter = new MoreAppListAdapter(getActivity(), oldRecentlyApp, handler);
			recen_listview.setAdapter(appGridAdapter);
			recen_listview.onRefreshComplete();
			stopLoad();
		}

		@Override
		public void onPreExecute() {

		}
	}

	@Override
	public void onRefresh() {
		showAsyncTask();
		// new Thread(){
		// // new
		// }.start();
	}

	@Override
	public void onLoadMore() {
		recen_listview.onLoadMoreComplete(false);
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		// recen_listview.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		onRefresh();
	}

	@Override
	public void onStop() {
//		appGridAdapter.recycleBitmap();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		appGridAdapter.recycleBitmap();
	}

	@Override
	public void onResume() {
		super.onResume();
		showAsyncTask();
	}

}
