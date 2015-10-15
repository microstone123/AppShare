package com.hyk.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @ClassName: AsyncHttpUtil
 * @Description: TODO(AsyncHttp请求工具类)
 * @author linhs
 * @date 2013-12-13 下午12:00:26
 */
public class AsyncHttpUtil {
	private static int CLERR_TIME = 1000 * 30;
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象

	static {
		client.setTimeout(CLERR_TIME); // 设置链接超时，如果不设置，默认为10s
	}

	/**
	 * 不带参数。用一个完整url获取一个string对象 get
	 */
	public static void getString(String urlString, AsyncHttpResponseHandler res) {
		client.get(urlString, res);
//		client.cancelRequests(arg0, arg1);
	}

	// url里面带参数 get
	public static void getString(String urlString, RequestParams params, AsyncHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	/**
	 * 不带参数。用一个完整url获取一个string对象
	 */
	public static void postString(String urlString, AsyncHttpResponseHandler res) {
		client.post(urlString, res);
	}

	// url里面带参数
	public static void postString(String urlString, RequestParams params, AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	// 不带参数，获取json对象或者数组
	public static void getJson(String urlString, JsonHttpResponseHandler res) {
		client.get(urlString, res);
	}

	// 带参数，获取json对象或者数组
	public static void getJson(String urlString, RequestParams params, JsonHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	// 不带参数，获取json对象或者数组
	public static void postJson(String urlString, JsonHttpResponseHandler res) {
		client.post(urlString, res);
	}

	// 带参数，获取json对象或者数组
	public static void postJson(String urlString, RequestParams params, JsonHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	// 下载数据使用，会返回byte数据
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
	
	//上传文件
	public static void postImg(String uString,RequestParams params,AsyncHttpResponseHandler res){
		client.post(uString, params, res);
	}
	
	// 取消请求
	public static void cancelRequests(Context context) {
		client.cancelRequests(context, true);
	}
}
