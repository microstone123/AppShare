package com.hyk.activity.app;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.hyk.activity.R;
import com.hyk.activity.photo.ImagePagerActivity;
import com.hyk.app.InfoOfApp;
import com.hyk.baseadapter.GridViewAdapter;
import com.hyk.broadcast.ConnectionChangeReceiver;
import com.hyk.download.DownloadConstants;
import com.hyk.download.DownloadForHttp;
import com.hyk.download.DownloadHandle;
import com.hyk.download.DownloadManagerDatabaseUtil;
import com.hyk.download.DownloadManagerInfo;
import com.hyk.download.DownloadManagerPro;
import com.hyk.fragment.ErrorConnetFragment;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForAppInfo;
import com.hyk.utils.AppManager;
import com.hyk.utils.CHString;
import com.hyk.utils.DisplayUtil;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.SharedPreferencesHelper;
import com.hyk.utils.ToastUtil;
import com.hyk.view.CustomMarqueeTextView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;

/**
 * @ClassName: AppDetailsActivity
 * @Description: TODO(应用详情界面)
 * @author linhs
 * @date 2013-12-6 下午2:28:40
 */

public class AppDetailsActivity extends FragmentActivity implements OnClickListener {

	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private static final int ERR_NETWORK = 1002;
	private static final int SUCC_NETWORK = 1001;
	public ImageButton btn_back;
	private String brief_text;
	private GridView mInfoGridView;
	private InfoOfApp infoOfApp = new InfoOfApp();
	private NetworkImageView detail_logo_image;
	private CustomMarqueeTextView details_name, detail_name;
	private TextView detail_down_num, detail_size, the_de_text;
	// private ImageLoader imageLoader = ImageLoader.getInstance();
	// protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private RelativeLayout detail_relat;
	private boolean isOpenText = true;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	public String appId;
	// private LoadDialog loadDialog;
	private RelativeLayout line_relat;
	private ImageButton detait_up_btn;
	public FrameLayout framelayout;

	private DownloadManagerPro downloadManagerPro;
	private static final int DOWN_TILE = 1010;
	private int downId = -1;
	private DownloadManagerDatabaseUtil downloadManagerDatabaseUtil;
	private int totalSize;
	private int currentSize;
	private String appName;
	private ProgressBar diao_progressBar;
	private Button diao_dowble;
	private GridViewAdapter mGalleryAdapter;
	private SharedPreferencesHelper spqq;
//	private ImageLoaderShow imageLoaderShow;
//	private RequestQueue requestQueue;

	private StringRequest jsonObjectRequest;
	private String regId, appForRegId,rImei;

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
			case DOWN_TILE:
				if (downId == msg.arg1) {
					int[] data = (int[]) msg.obj;
					int statics = data[2];
					downId = msg.arg1;
					totalSize = data[1];
					currentSize = data[0];
					Log.e("下载", downId + " " + data[2] + "  " + data[1] + "  " + data[0]);
					diao_progressBar.setMax(data[1]);
					diao_progressBar.setProgress(data[0]);

					if (statics == DownloadConstants.STATUS_FAILED) {
						statics = downloadManagerPro.getErrorCode(downId);
					}

					if (data[1] == data[0] && data[0] != -1 && data[0] != 0) {
						diao_dowble.setText(CHString.DOWN_INSTALLATION);
						downloadManagerDatabaseUtil.update(totalSize, currentSize, 8, downId);
						XmppApplication.downloadsChangeObserver.removeHandlerById(msg.arg1);
					}

					switch (statics) {
					case DownloadConstants.ERROR_CANNOT_RESUME:
						ToastUtil.show(AppDetailsActivity.this, CHString.STATUS_FAILED);
						diao_dowble.setText(CHString.DOWN_RUMM);
						break;
					case DownloadConstants.STATUS_SUCCESSFUL:
						// diao_dowble.setText(CHString.DOWN_INSTALLATION);
						// downloadManagerDatabaseUtil.update(totalSize,
						// currentSize, 8, downId);
						// DownloadConstants.installationApp(context,
						// downloadManagerPro.getFileUriString(downId,
						// appName));
						break;
					case DownloadConstants.STATUS_PAUSED:
						diao_dowble.setText(CHString.APP_CONTINUE);
						break;
					case DownloadConstants.ERROR_INSUFFICIENT_SPACE:
						ToastUtil.show(AppDetailsActivity.this, CHString.ERROR_INSUFFICIENT_SPACE);
						diao_dowble.setText(CHString.DOWN_RUMM);
						break;
					case DownloadConstants.ERROR_DEVICE_NOT_FOUND:
						ToastUtil.show(AppDetailsActivity.this, CHString.NOT_SDKA);
						diao_dowble.setText(CHString.DOWN_RUMM);
						break;
					case DownloadConstants.ERROR_FILE_ALREADY_EXISTS:
						diao_progressBar.setMax(100);
						diao_progressBar.setProgress(100);
						diao_dowble.setText(CHString.DOWN_INSTALLATION);
						downloadManagerDatabaseUtil.update(totalSize, currentSize, 8, downId);
						DownloadConstants.installationApp(AppDetailsActivity.this,
								downloadManagerPro.getFileUriString(downId, appName));
						ToastUtil.show(AppDetailsActivity.this, CHString.ERROR_FILE_ALREADY_EXISTS);
						break;
					case DownloadConstants.ERROR_URL:
						ToastUtil.show(AppDetailsActivity.this, CHString.ERROR_URL);
						diao_dowble.setText(CHString.STATUS_FAILED);
						break;
					default:
						break;
					}
				}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_details);
		AppManager.getAppManager().addActivity(this);
		downloadManagerPro = new DownloadManagerPro(this);
		downloadManagerDatabaseUtil = new DownloadManagerDatabaseUtil(this);
		int get_return = getIntent().getExtras().getInt("get_return");
		appId = getIntent().getExtras().getString("appId");
		rImei = getIntent().getExtras().getString("rImei");
		appForRegId = getIntent().getExtras().getString("appForRegId");
		spqq = new SharedPreferencesHelper(this, "loginInfo");
		if (StringUtils.isNotEmpty(spqq.getValue("redId"))) {
			regId = spqq.getValue("redId");
		}
//		requestQueue = Volley.newRequestQueue(this);
//		imageLoaderShow = new ImageLoaderShow(requestQueue);

		setupViews();
		if (get_return == 1) {
			infoOfApp = (InfoOfApp) getIntent().getSerializableExtra("infoOfApp");
			startLoad();
			setData(infoOfApp);
			detail_relat.setVisibility(View.VISIBLE);
		} else {
			detail_relat.setVisibility(View.GONE);
			startLoad();
			// getHttp(appId);
			httpForData();
			// requestQueue.cancelAll("httpForAppData");
		}

	}

	public void setupViews() {
		framelayout = (FrameLayout) findViewById(R.id.framelayout_err_connet);
		ReplaceFragmentMethod(new ErrorConnetFragment());

		detait_up_btn = (ImageButton) findViewById(R.id.detait_up_btn);
		line_relat = (RelativeLayout) findViewById(R.id.line_relat);
		detail_relat = (RelativeLayout) findViewById(R.id.detail_relat);
		diao_progressBar = (ProgressBar) findViewById(R.id.detail_progressBar);
		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);
		btn_back = (ImageButton) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		detail_name = (CustomMarqueeTextView) findViewById(R.id.detail_name);
		details_name = (CustomMarqueeTextView) findViewById(R.id.details_name);
		detail_down_num = (TextView) findViewById(R.id.detail_down_num);
		detail_size = (TextView) findViewById(R.id.detail_size);
		the_de_text = (TextView) findViewById(R.id.the_de_text);
		detail_logo_image = (NetworkImageView) findViewById(R.id.detail_logo_image);
		mInfoGridView = (GridView) findViewById(R.id.dmInfoGridView);
		diao_dowble = (Button) findViewById(R.id.detail_dowble);
		diao_dowble.setOnClickListener(this);
		line_relat.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, this);
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			break;
		case R.id.line_relat:
			if (isOpenText) {
				isOpenText = false;
				// the_de_text brief_text
				if (brief_text.length() > 40) {
					String contentte = brief_text.substring(0, 40);
					detait_up_btn.setBackgroundResource(R.drawable.down_btn);
					the_de_text.setText(contentte + "...");
				}
			} else {
				isOpenText = true;
				detait_up_btn.setBackgroundResource(R.drawable.up_btn);
				the_de_text.setText(brief_text);
			}
			break;
		case R.id.detail_dowble:
			if (StringUtils.isNotEmpty(infoOfApp.getDownUrl())) {
				if (CHString.DOWN_STR.equals(diao_dowble.getText().toString())) {

					TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					DownloadForHttp.httpForDownapp(appId, regId, appForRegId, telManager.getDeviceId(),rImei);

					downId = downloadManagerPro.startDown(infoOfApp.getDownUrl(), handler, DOWN_TILE, appName);
					diao_dowble.setText(CHString.APP_PAUSE);
					XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, handler, infoOfApp
							.getDownUrl(), appName, 0));

				} else if (CHString.APP_PAUSE.equals(diao_dowble.getText().toString())) {
					downloadManagerPro.pauseDownload(totalSize, currentSize, downId);
					XmppApplication.downloadsChangeObserver.removeHandlerById(downId);
					diao_dowble.setText(CHString.APP_CONTINUE);
				} else if (CHString.APP_CONTINUE.equals(diao_dowble.getText().toString())) {
					downloadManagerPro.resumeDownload(handler, downId, DOWN_TILE);
					XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, handler, infoOfApp
							.getDownUrl(), appName, 0));
					diao_dowble.setText(CHString.APP_PAUSE);
				} else if (CHString.DOWN_INSTALLATION.equals(diao_dowble.getText().toString())) {
					Uri fileUri = downloadManagerPro.getFileUriString(downId, appName);
					if (fileUri != null) {
						DownloadConstants.installationApp(AppDetailsActivity.this, fileUri);
					} else {
						downId = downloadManagerPro.startDown(infoOfApp.getDownUrl(), handler, DOWN_TILE, appName);
						diao_dowble.setText(CHString.APP_PAUSE);
					}
				} else if (CHString.DOWN_RUMM.equals(diao_dowble.getText().toString())) {
					if (downId > 0) {
						downloadManagerPro.restartDownload(handler, DOWN_TILE, downId);
					} else {
						downId = downloadManagerPro.startDown(infoOfApp.getDownUrl(), handler, DOWN_TILE, appName);
					}
					diao_dowble.setText(CHString.APP_PAUSE);
				}
			} else {
				ToastUtil.show(AppDetailsActivity.this, CHString.ERROR_SEARCH);
			}
			break;
		default:
			break;
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

	/**
	 * 加载数据
	 */
	private void setData(InfoOfApp appInfo) {

		stopLoad();
		try {
			detail_logo_image.setDefaultImageResId(R.drawable.transparent_d);
			detail_logo_image.setErrorImageResId(R.drawable.transparent_d);
			detail_logo_image.setImageUrl(appInfo.getAppImage(), XmppApplication.getsInstance().imageLoader);
		} catch (OutOfMemoryError e) {
		}
		appId = appInfo.getId();
		appName = appInfo.getName();
		detail_name.setText(appName);
		details_name.setText(appName);
		detail_size.setText(appInfo.getParamsSize());
		String str = appInfo.getBriefIntroduction();
		str = str.replaceAll(" ", "");
		str = str.replaceAll("<br/>", "\t");
		str = str.replaceAll("<br />", "\t");
		brief_text = str;
		if (brief_text.length() < 48) {
			line_relat.setVisibility(View.INVISIBLE);
		}

		the_de_text.setText(brief_text);
		String downnu = (double) appInfo.getDownNum() / 10000 + "";
		detail_down_num.setText(downnu + CHString.THOUSAND_STR);
		final String[] imgURLList = appInfo.getImgUrls().split(";");

		mGalleryAdapter = new GridViewAdapter(AppDetailsActivity.this, imgURLList);
		mInfoGridView.setAdapter(mGalleryAdapter);
		mInfoGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		LayoutParams params = new LayoutParams(mGalleryAdapter.getCount()
				* DisplayUtil.dip2px(AppDetailsActivity.this, 145), DisplayUtil.dip2px(AppDetailsActivity.this, 250));
		mInfoGridView.setLayoutParams(params);
		mInfoGridView.setStretchMode(GridView.NO_STRETCH);
		mInfoGridView.setNumColumns(mGalleryAdapter.getCount());

		mInfoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ViewSetClick.getAlpha(view, AppDetailsActivity.this);
				imageBrower(position, imgURLList);
			}
		});
		DownloadManagerInfo downloadManagerInfo = downloadManagerDatabaseUtil.queryForUrl(appInfo.getDownUrl());

		if (downloadManagerInfo != null) {
			downId = downloadManagerInfo.getDownId();
			if (StringUtils.isNotEmpty(downloadManagerInfo.getTotalSize())) {
				diao_progressBar.setMax(Integer.valueOf(downloadManagerInfo.getTotalSize()));
			}
			if (StringUtils.isNotEmpty(downloadManagerInfo.getCurrentSize())) {
				diao_progressBar.setProgress(Integer.valueOf(downloadManagerInfo.getCurrentSize()));
			}
			if (downloadManagerInfo.getStatics() == 4) {
				diao_dowble.setText(CHString.APP_CONTINUE);
			} else if (downloadManagerInfo.getStatics() == 8) {
				diao_dowble.setText(CHString.DOWN_INSTALLATION);
				diao_progressBar.setMax(100);
				diao_progressBar.setProgress(100);
			} else if (downloadManagerInfo.getStatics() == 2) {
				diao_dowble.setText(CHString.APP_PAUSE);
				XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, handler, appInfo
						.getDownUrl(), appName, 0));
			}
		}
	}

	/**
	 * 加载初始进入Fragment的方法
	 */
	private void ReplaceFragmentMethod(Fragment fragment) {
		FragmentTransaction tration = getSupportFragmentManager().beginTransaction();
		tration.replace(R.id.framelayout_err_connet, fragment);
		tration.commitAllowingStateLoss();
	}

	private void httpForData() {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APPID_ADDRESS;
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", appId);
		String httpUrl = HttpUtils.checkUrl(url, params);

		jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				stopLoad();
				ResultStringForAppInfo resultString = new ResultStringForAppInfo();
				resultString = binder.fromJson(response, ResultStringForAppInfo.class);
				if (resultString != null) {
					infoOfApp = resultString.getObj();
					setData(infoOfApp);
				} else {
					finish();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ToastUtil.show(AppDetailsActivity.this, CHString.NETWORK_ERROR);
			}
		});
//		jsonObjectRequest.setTag("httpForAppData");
//		requestQueue.add(jsonObjectRequest);
		XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest,"httpForAppData");
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		detail_relat.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		detail_relat.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForAppData");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onResume() {
		ConnectionChangeReceiver.setHandler(handler);
		if (!NetworkUtil.checkNetworkState(this)) {
			framelayout.setVisibility(View.VISIBLE);
		} else {
			framelayout.setVisibility(View.GONE);
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			XmppApplication.getsInstance().cancelPendingRequests("httpForAppData");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
