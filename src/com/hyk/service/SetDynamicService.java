package com.hyk.service;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Message;

import com.hyk.http.AppShowHttp;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.utils.BimpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.view.Base64;
import com.hyk.view.Image;

/**
 * 获取个人动态
 * 
 * @author Administrator
 */
public class SetDynamicService extends Service {

	private String redId;
	private String content;
	private ArrayList<Image> upLoads = new ArrayList<Image>();
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private int httpNum = 0;
	private static final int SUCC_DY = 1003;
	private static final int ERR_DY = 1004;
	private String send = "";
	private boolean isOk = true;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		content = intent.getExtras().getString("dy_content");
		redId = intent.getExtras().getString("dy_redId");
		send = null;
		startAsync();
		return START_REDELIVER_INTENT;
	}

	// private void httpForSetdynamic() {
	// upLoads.clear();
	// if (StringUtils.isNotEmpty(send)) {
	// for (int i = 0; i < BimpUtils.drr.size(); i++) {
	// try {
	// Image image = new Image();
	// image.setPath(Base64.encodeFromFile(BimpUtils.drr.get(i).toString()));
	// upLoads.add(image);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// if (upLoads.size() > 0) {
	// send = binder.toJson(upLoads);
	// }
	// }
	// String url = Httpaddress.IP_ADDRESS + Httpaddress.SEND_NEWS;
	// // String url = "http://192.168.0.32:8080/" + Httpaddress.SEND_NEWS;
	// RequestParams params = new RequestParams();
	// params.put("content", content);
	// params.put("size", send.length() + "");
	// params.put("images", send);
	// params.put("regId", redId);
	// ToastUtil.show(this, "postJson");
	// AsyncHttpUtil.postJson(url, params, new JsonHttpResponseHandler() {
	// public void onSuccess(JSONObject arg0) { // 返回的是JSONObject，会调用这里
	// ToastUtil.show(SetDynamicService.this, "onSuccess " + arg0.toString());
	// ResultStringForLoadap resultString = new ResultStringForLoadap();
	// resultString = binder.fromJson(arg0.toString(),
	// ResultStringForLoadap.class);
	// if (resultString != null) {
	// if (resultString.getResult() != 0) {
	// httpNum++;
	// if (httpNum < 3) {
	// httpForSetdynamic();
	// } else {
	// goBack(SUCC_DY);
	// }
	// } else {
	// goBack(SUCC_DY);
	// }
	// }
	// };
	//
	// public void onFailure(Throwable arg0) {
	// ToastUtil.show(SetDynamicService.this, "onFailure " + arg0.getMessage());
	// if (NetworkUtil.checkNetworkState(SetDynamicService.this)) {
	// httpNum++;
	// if (httpNum < 3) {
	// httpForSetdynamic();
	// } else {
	// goBack(ERR_DY);
	// }
	// } else {
	// ToastUtil.show(SetDynamicService.this, CHString.ERROR_SETCOMMENT);
	// }
	// };
	//
	// public void onFinish() {
	// };
	// });
	//
	// }

	private void goBack(int key) {
		Message msg = new Message();
		msg.what = key;
		BimpUtils.handler.sendMessage(msg);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class AsyncTaskUtil extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			upLoads.clear();
			if (StringUtils.isEmpty(send)) {
				for (int i = 0; i < BimpUtils.drr.size(); i++) {
					try {
						Image image = new Image();
						image.setPath(Base64.encodeFromFile(BimpUtils.drr.get(i).toString()));
						upLoads.add(image);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (upLoads.size() > 0) {
					send = binder.toJson(upLoads);
				}
			}

			String resultStr = AppShowHttp.httpForSetdynamic(SetDynamicService.this, content, redId, send);
			if (StringUtils.isNotEmpty(resultStr)) {
				ResultStringForLoadap resultString = new ResultStringForLoadap();
				resultString = binder.fromJson(resultStr, ResultStringForLoadap.class);
				if (resultString == null) {
					isOk = false;
				} else {
					if (resultString.getResult() == 0) {
						isOk = true;
					} else {
						isOk = false;
					}
				}
			}else{
				isOk = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (!isOk) {
				httpNum++;
				if (httpNum < 3) {
					startAsync();
				} else {
					goBack(ERR_DY);
					send = null;
				}
			} else {
				goBack(SUCC_DY);
			}
		}

		@Override
		public void onPreExecute() {

		}
	}

	private void startAsync() {
		AsyncTaskUtil asyncTaskUtil = new AsyncTaskUtil();
		asyncTaskUtil.execute();
	}

}
