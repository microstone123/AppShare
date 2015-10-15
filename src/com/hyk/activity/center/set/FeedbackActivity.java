package com.hyk.activity.center.set;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.R;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;

public class FeedbackActivity extends FragmentActivity implements OnClickListener {

	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public FrameLayout framelayout;

	private EditText feed_text;
	private ImageButton btn_back;
	private Button set_feed;
	private int textNum = 120;
	private SharedPreferencesHelper spqq;
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;

	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private boolean isSet = false;
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERR_NETWORK:
				framelayout.setVisibility(View.VISIBLE);
				break;
			case SUCC_NETWORK:
				framelayout.setVisibility(View.GONE);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.set_feed);
		spqq = new SharedPreferencesHelper(this, "loginInfo");
//		requestQueue = Volley.newRequestQueue(this);
		AppManager.getAppManager().addActivity(this);
		setupViews();
	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());
		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);
		set_feed = (Button) findViewById(R.id.set_feed);
		feed_text = (EditText) findViewById(R.id.feed_text);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		set_feed.setOnClickListener(this);

		feed_text.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			private int selectionStart;
			private int selectionEnd;
			private int MAXLINES = 12;

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
				selectionStart = feed_text.getSelectionStart();
				selectionEnd = feed_text.getSelectionEnd();
				int lines = feed_text.getLineCount();
				if (lines > MAXLINES) {
					ToastUtil.show(FeedbackActivity.this, CHString.MAX_LINES);
					s.delete(selectionStart - 1, selectionEnd);
					int tempSelection = selectionEnd;
					feed_text.setText(s);
					feed_text.setSelection(tempSelection);// 设置光标在最后
				} else {
					if (temp.length() > textNum) {
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionEnd;
						feed_text.setText(s);
						feed_text.setSelection(tempSelection);// 设置光标在最后
					} else {

					}
				}
			}
		});
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			ViewSetClick.getAlpha(btn_back, this);
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		case R.id.set_feed:
			if (!isSet) {
				ViewSetClick.getAlpha(set_feed, this);
				if (StringUtils.isNotEmpty(feed_text.getText().toString())) {
					isSet = true;
					if (NetworkUtil.checkNetworkState(this)) {
						startLoad();
					} else {
						ToastUtil.show(this, CHString.NETWORK_ERROR);
					}
				}else{
					ToastUtil.show(this, CHString.ERROR_FEED);
				}
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 请求附近应用数据
	 */
	private void httpForFeedback() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_FEEDBACK;
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", spqq.getValue("redId"));
		params.put("content", feed_text.getText().toString());
		String httpUrl = HttpUtils.checkUrl(url, params);
		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				stopLoad();
				isSet = false;
				ResultStringForLoadap resultString = new ResultStringForLoadap();
				resultString = binder.fromJson(response, ResultStringForLoadap.class);
				if (resultString != null) {
					if (resultString.getResult() == 0) {
						finish();
						overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
					}
				}
				
				ToastUtil.show(FeedbackActivity.this, CHString.SUCC_FEED);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (NetworkUtil.checkNetworkState(FeedbackActivity.this)) {
					httpForFeedback();
				} else {
					stopLoad();
					ToastUtil.show(FeedbackActivity.this, CHString.NETWORK_ERROR);
				}
			}
		});
//		jsonObjectRequest.setTag("httpForFeedback");
//		requestQueue.add(jsonObjectRequest);
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "httpForFeedback");
	}

	@Override
	protected void onResume() {
		isSet = false;
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForFeedback");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForFeedback");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		httpForFeedback();
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
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

}
