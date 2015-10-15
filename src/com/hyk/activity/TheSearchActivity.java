package com.hyk.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.android.volley.toolbox.NetworkImageView;
import com.hyk.activity.app.AppDetailsActivity;
import com.hyk.app.AppHotList;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.http.AppShowHttp;
import com.hyk.resultforjson.ResultStringForHot;
import com.hyk.utils.ACache;
import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.RandomUtil;
import com.hyk.utils.ToastUtil;
import com.hyk.view.KeywordsView;
import com.hyk.view.ShakeListener;
import com.hyk.view.ShakeListener.OnShakeListener;
import com.hyk.view.ViewSetClick;

public class TheSearchActivity extends FragmentActivity implements OnShakeListener, OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;
	public ImageButton btn_back;
	private ACache mCache;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private KeywordsView the_search_range;
	private List<AppHotList> appList = new ArrayList<AppHotList>();
	private GestureDetector mggd;
	private EditText tv_searchInput;
	private Button btn_search;
	private ShakeListener mShakeListener = null;
	private List<AppHotList> cacheList = new ArrayList<AppHotList>();

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				if (cacheList != null) {
					if (cacheList.size() <= 0) {
						AsyncTaskUtil asyncTaskUtil = new AsyncTaskUtil();
						asyncTaskUtil.execute();
					}
				} else {

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
		setContentView(R.layout.the_search);
		AppManager.getAppManager().addActivity(this);
		mCache = ACache.get(this);
		setupViews();
	}

	@SuppressWarnings("deprecation")
	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		tv_searchInput = (EditText) findViewById(R.id.tv_searchInput);
		btn_search = (Button) findViewById(R.id.btn_search);
		the_search_range = (KeywordsView) findViewById(R.id.the_search_range);
		the_search_range.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		the_search_range.setDuration(800l);
		this.mggd = new GestureDetector(new Mygdlinseter());
		the_search_range.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mggd.onTouchEvent(event); // 注册点击事件
			}
		});
		// mShakeListener = new ShakeListener(this);
		// mShakeListener.setOnShakeListener(this);

		tv_searchInput.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int textNum = 14;
			private int MAXLINES = 1;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				selectionStart = tv_searchInput.getSelectionStart();
				selectionEnd = tv_searchInput.getSelectionEnd();
				int lines = tv_searchInput.getLineCount();
				if (lines > MAXLINES) {
					s.delete(selectionStart - 1, selectionEnd);
					tv_searchInput.setText(s);
					Editable b = tv_searchInput.getText();
					tv_searchInput.setSelection(b.length());
				} else {
					if (temp.length() > textNum) {
						s.delete(selectionStart - 1, selectionEnd);
						tv_searchInput.setText(s);
						Editable b = tv_searchInput.getText();
						tv_searchInput.setSelection(b.length());
					}
				}
			}
		});

	}

	@Override
	protected void onResume() {
		Runtime.getRuntime().gc();
		tv_searchInput.setText("");
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		if (mShakeListener == null) {
			mShakeListener = new ShakeListener(this);
			mShakeListener.setOnShakeListener(this);
		}
//		the_search_range.newImageLoader(this);
		AsyncTaskUtil asyncTaskUtil = new AsyncTaskUtil();
		asyncTaskUtil.execute();
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		if (mShakeListener != null) {
			mShakeListener.stop();
			mShakeListener = null;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, this);
		if (v == btn_back) {
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
		}
		if (v instanceof NetworkImageView) {
			if (mShakeListener != null) {
				mShakeListener.stop();
			}
			if (!NetworkUtil.checkNetworkState(this)) {
				ToastUtil.show(this, CHString.NETWORK_ERROR);
			} else {

				if (v.getTag() != null) {

					int appId = (Integer) v.getTag();
					Intent intent = new Intent(this, AppDetailsActivity.class);
					intent.putExtra("appId", String.valueOf(appId));
					intent.putExtra("get_return", 0);
					intent.putExtra("appForRegId", 0);
					intent.putExtra("rImei", 0);
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//					finish();
				}
			}
		}
		if (v == btn_search) {
			String app_name = tv_searchInput.getText().toString();
			if (StringUtils.isNotEmpty(app_name)) {
				Intent intent = new Intent(this, SearchOflistActivity.class);
				intent.putExtra("app_name", tv_searchInput.getText().toString());
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			} else {
				ToastUtil.show(this, CHString.SEARCH_NOT_TEXT);
			}
		}
	}

	class Mygdlinseter implements OnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e2.getX() - e1.getX() > 10) { // 右滑
				if (!NetworkUtil.checkNetworkState(TheSearchActivity.this)) {
					ToastUtil.show(TheSearchActivity.this, CHString.NETWORK_ERROR);
				} else {
					appList = cacheList;
					openFeet(appList, true);
					// AsyncTaskUtil asyncTaskUtil = new AsyncTaskUtil();
					// asyncTaskUtil.execute();
				}
				return true;
			}
			if (e2.getX() - e1.getX() < -10) {// 左滑
				if (!NetworkUtil.checkNetworkState(TheSearchActivity.this)) {
					ToastUtil.show(TheSearchActivity.this, CHString.NETWORK_ERROR);
				} else {
					appList = cacheList;
					openFeet(appList, true);
					// AsyncTaskUtil asyncTaskUtil = new AsyncTaskUtil();
					// asyncTaskUtil.execute();
				}
				return true;
			}
			return false;
		}
	}

	/**
	 * 显示云标签
	 */
	private void openFeet(List<AppHotList> list, boolean isStart) {
		the_search_range.rubKeywords();
		feedKeywordsFlow(the_search_range, RandList(list));
		if (isStart) {
			the_search_range.go2Shwo(KeywordsView.ANIMATION_OUT);
		}

	}

	/**
	 * 随机获取AppList
	 */
	public List<AppHotList> RandList(List<AppHotList> list) {
		if (list != null) {
			if (list.size() > 0) {
				List<AppHotList> newList = new ArrayList<AppHotList>();
				int[] intArry = RandomUtil.getExchangeCode(list.size(), 10);
				if (list != null) {
					if (list.size() > 0) {
						for (int i = 0; i < intArry.length; i++) {
							AppHotList appHotList = new AppHotList();
							appHotList = list.get(intArry[i]);
							newList.add(appHotList);
						}
					}
				}
				return newList;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 添加云标签
	 * 
	 * @param keyworldFlow
	 * @param arr
	 */
	private void feedKeywordsFlow(KeywordsView keyworldFlow, List<AppHotList> appList) {
		if (appList != null) {
			if (appList.size() > 0) {
				for (int i = 0; i < KeywordsView.MAX; i++) {
					AppHotList tmp = appList.get(i);
					keyworldFlow.feedKeyword(tmp);
				}
			}
		}
	}

	/**
	 * 获取热门app列表
	 */
	public List<AppHotList> setAppList(String arg0) {
		ResultStringForHot resultString = new ResultStringForHot();
		resultString = binder.fromJson(arg0, ResultStringForHot.class);
		if (resultString != null) {
			appList = resultString.getObj();
		}
		return RandList(appList);
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

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	@Override
	public void onShake() {
		if (!NetworkUtil.checkNetworkState(this)) {
			mShakeListener.stop();
			mShakeListener = null;
			ToastUtil.show(this, CHString.NETWORK_ERROR);
		} else {
			mShakeListener.stop();
			mShakeListener.start();
			appList = cacheList;
			if (appList == null) {
				// httpForHot();
			} else {
				openFeet(appList, true);
			}
		}
	}

	/**
	 * 请求应用评论信息
	 */
	private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				the_search_range.go2Shwo(KeywordsView.ANIMATION_OUT);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		@Override
		public void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... params) {

			String hotJson = mCache.getAsString("hot_string");
			if (StringUtils.isNotEmpty(hotJson)) {
				appList = setAppList(hotJson);
				cacheList = appList;
				openFeet(appList, false);
			} else {
				String arfo = AppShowHttp.httpForHotApp(TheSearchActivity.this);
				appList = setAppList(arfo);
				if (appList != null) {
					openFeet(appList, false);
				}
			}
			return null;

		}

	}

	@Override
	protected void onStop() {
//		the_search_range.clearMemoryCache();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		the_search_range.recycleBitmap();
		Runtime.getRuntime().gc();
//		ToastUtil.show(this, "onDestroy");
	}
	
	

}
