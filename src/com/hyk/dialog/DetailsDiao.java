package com.hyk.dialog;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.activity.app.AppDetailsActivity;
import com.hyk.app.InfoOfApp;
import com.hyk.download.DownloadConstants;
import com.hyk.download.DownloadForHttp;
import com.hyk.download.DownloadHandle;
import com.hyk.download.DownloadManagerDatabaseUtil;
import com.hyk.download.DownloadManagerInfo;
import com.hyk.download.DownloadManagerPro;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForAppInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.NetworkUtil;
import com.hyk.utils.ToastUtil;
import com.hyk.view.RoundedImageView;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @ClassName: AppdiaosActivity
 * @Description: TODO(应用简介界面)
 * @author linhs
 * @date 2013-12-6 下午2:28:40
 */

@SuppressLint("HandlerLeak")
public class DetailsDiao extends Dialog implements android.view.View.OnClickListener {

	private RelativeLayout loading_relat;
	private ImageView loading_img;
	private AnimationDrawable animationDrawable;
	private int appId;
	private RoundedImageView diao_logo_image;
	private TextView diao_name, diao_size, app_down_num, app_diao_text;
	private ProgressBar diao_progressBar;
	private Button diao_dowble;
	// private FileDownloader fileDownloader;
	private Handler handler;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private RelativeLayout diao_relat, diao_info_top;
	private String brief_text;
	// private ImageView diao_star_01, diao_star_02, diao_star_03, diao_star_04,
	// diao_star_05;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private Context context;
	private InfoOfApp infoOfApp = new InfoOfApp();
	// AnimateFirstDisplayListener();
	private ACache mCache;

	private DownloadManagerPro downloadManagerPro;
	private static final int DOWN_TILE = 1010;
	private int downId = -1;
	private DownloadManagerDatabaseUtil downloadManagerDatabaseUtil;
	private int totalSize;
	private int currentSize;
	private String appName,regId, appForRegId;
//	private RequestQueue requestQueue;
	private StringRequest jsonObjectRequest;
	private String appImei;

	public Handler diaoHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
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
						ToastUtil.show(context, CHString.STATUS_FAILED);
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
						ToastUtil.show(context, CHString.ERROR_INSUFFICIENT_SPACE);
						diao_dowble.setText(CHString.DOWN_RUMM);
						break;
					case DownloadConstants.ERROR_DEVICE_NOT_FOUND:
						ToastUtil.show(context, CHString.NOT_SDKA);
						diao_dowble.setText(CHString.DOWN_RUMM);
						break;
					case DownloadConstants.ERROR_FILE_ALREADY_EXISTS:
						diao_progressBar.setMax(100);
						diao_progressBar.setProgress(100);
						diao_dowble.setText(CHString.DOWN_INSTALLATION);
						downloadManagerDatabaseUtil.update(totalSize, currentSize, 8, downId);
						DownloadConstants
								.installationApp(context, downloadManagerPro.getFileUriString(downId, appName));
						ToastUtil.show(context, CHString.ERROR_FILE_ALREADY_EXISTS);
						break;
					case DownloadConstants.ERROR_URL:
						ToastUtil.show(context, CHString.ERROR_URL);
						diao_dowble.setText(CHString.STATUS_FAILED);
						break;
					default:
						break;
					}
					break;
				}
			}
		}
	};

	public DetailsDiao(Context context, Handler handler, int appId,String regId,String appForRegId,String appImei) {
		super(context, R.style.customDialog);
		setCancelable(false); // 阻止返回键的响应
		setContentView(R.layout.app_details_diao);
		this.appId = appId;
		this.handler = handler;
		this.context = context;
		this.regId = regId;
		this.appImei = appImei;
		this.appForRegId=appForRegId;
//		requestQueue = Volley.newRequestQueue(context);
		mCache = ACache.get(context);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
		downloadManagerPro = new DownloadManagerPro(context);
		downloadManagerDatabaseUtil = new DownloadManagerDatabaseUtil(context);
		setupViews();
		startLoad();
	}

	public void setupViews() {
		diao_progressBar = (ProgressBar) findViewById(R.id.diao_progressBar);
		diao_name = (TextView) findViewById(R.id.diao_name);
		diao_size = (TextView) findViewById(R.id.diao_size);
		app_down_num = (TextView) findViewById(R.id.app_down_num);
		app_diao_text = (TextView) findViewById(R.id.app_detail_text);
		diao_logo_image = (RoundedImageView) findViewById(R.id.diao_logo_image);
		diao_dowble = (Button) findViewById(R.id.diao_dowble);
		diao_relat = (RelativeLayout) findViewById(R.id.detail_relat);
		diao_info_top = (RelativeLayout) findViewById(R.id.diao_info_top);
		diao_info_top.setOnClickListener(this);
		diao_dowble.setOnClickListener(this);
		loading_relat = (RelativeLayout) findViewById(R.id.loading_relat);
		loading_img = (ImageView) findViewById(R.id.loading_img);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// downloadManagerPro.unRegisterContentObserver();
	}

	@Override
	public void onClick(View v) {
		ViewSetClick.getAlpha(v, context);
		switch (v.getId()) {
		case R.id.diao_info_top:
			Intent intent = new Intent(context, AppDetailsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("infoOfApp", infoOfApp);
			intent.putExtras(bundle);
			intent.putExtra("appId", infoOfApp.getId());
			intent.putExtra("get_return", 1);
			intent.putExtra("appForRegId", appForRegId);
			intent.putExtra("rImei", appImei);
			context.startActivity(intent);
			((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			this.dismiss();
			break;
			
		case R.id.diao_dowble:
			if (StringUtils.isNotEmpty(infoOfApp.getDownUrl())) {
				if (CHString.DOWN_STR.equals(diao_dowble.getText().toString())) {
					
					TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
					DownloadForHttp.httpForDownapp(String.valueOf(appId), regId, appForRegId, telManager.getDeviceId(),appImei);
					
					downId = downloadManagerPro.startDown(infoOfApp.getDownUrl(), diaoHandler, DOWN_TILE, appName);
					diao_dowble.setText(CHString.APP_PAUSE);
					XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, diaoHandler,
							infoOfApp.getDownUrl(), appName, 0));
				} else if (CHString.APP_PAUSE.equals(diao_dowble.getText().toString())) {
					downloadManagerPro.pauseDownload(totalSize, currentSize, downId);
					XmppApplication.downloadsChangeObserver.removeHandlerById(downId);
					diao_dowble.setText(CHString.APP_CONTINUE);
				} else if (CHString.APP_CONTINUE.equals(diao_dowble.getText().toString())) {
					downloadManagerPro.resumeDownload(diaoHandler, downId, DOWN_TILE);
					XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, diaoHandler,
							infoOfApp.getDownUrl(), appName, 0));
					diao_dowble.setText(CHString.APP_PAUSE);
				} else if (CHString.DOWN_INSTALLATION.equals(diao_dowble.getText().toString())) {
					Uri fileUri = downloadManagerPro.getFileUriString(downId, appName);
					if (fileUri != null) {
						DownloadConstants.installationApp(context, fileUri);
					} else {
						downId = downloadManagerPro.startDown(infoOfApp.getDownUrl(), diaoHandler, DOWN_TILE, appName);
						diao_dowble.setText(CHString.APP_PAUSE);
					}
				} else if (CHString.DOWN_RUMM.equals(diao_dowble.getText().toString())) {
					if (downId > 0) {
						downloadManagerPro.restartDownload(diaoHandler, DOWN_TILE, downId);
					} else {
						downId = downloadManagerPro.startDown(infoOfApp.getDownUrl(), diaoHandler, DOWN_TILE, appName);
					}
					diao_dowble.setText(CHString.APP_PAUSE);
				}
			} else {
				ToastUtil.show(context, CHString.ERROR_SEARCH);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 请求应用详情信息
	 */
	private void getdiaos() {
		if (NetworkUtil.checkNetworkState(context)) {
			String url = Httpaddress.IP_ADDRESS + Httpaddress.APPID_ADDRESS;
			Map<String, String> params = new HashMap<String, String>();
			params.put("id", String.valueOf(appId));
			String httpUrl = HttpUtils.checkUrl(url, params);
			jsonObjectRequest = new StringRequest(httpUrl, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					try {
						stopLoad();
						ResultStringForAppInfo resultString = new ResultStringForAppInfo();
						// InfoOfApp infoOfApp = new InfoOfApp();
						resultString = binder.fromJson(response, ResultStringForAppInfo.class);
						if (resultString != null) {
							infoOfApp = resultString.getObj();
							mCache.put("appinfo_" + appId, response);
							loadData(infoOfApp);
						} else {
							ToastUtil.show(context, CHString.NOT_APP);
							dismiss();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					stopLoad();
					ToastUtil.show(context, CHString.NETWORK_BUSY);
					dismiss();
				}
			});
//			jsonObjectRequest.setTag("getdiaos");
//			requestQueue.add(jsonObjectRequest);
			XmppApplication.getsInstance().addToRequestQueue(jsonObjectRequest, "getdiaos");
		}
	}

	/**
	 * 加载数据
	 */
	private void loadData(InfoOfApp appInfo) {

		appName = appInfo.getName();
		diao_name.setText(appName);
		diao_size.setText(appInfo.getParamsSize());
		String str = appInfo.getBriefIntroduction();
		str = str.replaceAll(" ", "");
		str = str.replaceAll("<br/>", "\t");
		brief_text = "\t" + str;
		app_diao_text.setText(brief_text);

		String downnu = (double) appInfo.getDownNum() / 10000 + "";

		app_down_num.setText(downnu + CHString.THOUSAND_STR);

		imageLoader.displayImage(appInfo.getAppImage(), diao_logo_image, options);

		DownloadManagerInfo downloadManagerInfo = downloadManagerDatabaseUtil.queryForUrl(appInfo.getDownUrl());

		if (downloadManagerInfo != null) {
			if (StringUtils.isNotEmpty(downloadManagerInfo.getTotalSize())) {
				diao_progressBar.setMax(Integer.valueOf(downloadManagerInfo.getTotalSize()));
			}
			if (StringUtils.isNotEmpty(downloadManagerInfo.getCurrentSize())) {
				diao_progressBar.setProgress(Integer.valueOf(downloadManagerInfo.getCurrentSize()));
			}
			downId = downloadManagerInfo.getDownId();
			if (downloadManagerInfo.getStatics() == 4) {
				diao_dowble.setText(CHString.APP_CONTINUE);
			} else if (downloadManagerInfo.getStatics() == 8) {
				diao_dowble.setText(CHString.DOWN_INSTALLATION);
				diao_progressBar.setMax(100);
				diao_progressBar.setProgress(100);
			} else if (downloadManagerInfo.getStatics() == 2) {
				diao_dowble.setText(CHString.APP_PAUSE);
				XmppApplication.downloadsChangeObserver.addHandler(new DownloadHandle(downId, diaoHandler, appInfo
						.getDownUrl(), appName, 0));
			}
		}

	}

	public void stopLoad() {
		loading_relat.setVisibility(View.GONE);
		diao_relat.setVisibility(View.VISIBLE);
		if (animationDrawable != null) {
			animationDrawable.stop();
		}
	}

	public void startLoad() {
		loading_relat.setVisibility(View.VISIBLE);
		diao_relat.setVisibility(View.GONE);
		animationDrawable = (AnimationDrawable) loading_img.getDrawable();
		animationDrawable.start();
		getdiaos();
	}

	@Override
	public void dismiss() {
		super.dismiss();
		try {
			XmppApplication.downloadsChangeObserver.setStatics(downId, 1);
			XmppApplication.getsInstance().cancelPendingRequests("getdiaos");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
