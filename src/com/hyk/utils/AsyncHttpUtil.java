package com.hyk.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @ClassName: AsyncHttpUtil
 * @Description: TODO(AsyncHttp���󹤾���)
 * @author linhs
 * @date 2013-12-13 ����12:00:26
 */
public class AsyncHttpUtil {
	private static int CLERR_TIME = 1000 * 30;
	private static AsyncHttpClient client = new AsyncHttpClient(); // ʵ��������

	static {
		client.setTimeout(CLERR_TIME); // �������ӳ�ʱ����������ã�Ĭ��Ϊ10s
	}

	/**
	 * ������������һ������url��ȡһ��string���� get
	 */
	public static void getString(String urlString, AsyncHttpResponseHandler res) {
		client.get(urlString, res);
//		client.cancelRequests(arg0, arg1);
	}

	// url��������� get
	public static void getString(String urlString, RequestParams params, AsyncHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	/**
	 * ������������һ������url��ȡһ��string����
	 */
	public static void postString(String urlString, AsyncHttpResponseHandler res) {
		client.post(urlString, res);
	}

	// url���������
	public static void postString(String urlString, RequestParams params, AsyncHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	// ������������ȡjson�����������
	public static void getJson(String urlString, JsonHttpResponseHandler res) {
		client.get(urlString, res);
	}

	// ����������ȡjson�����������
	public static void getJson(String urlString, RequestParams params, JsonHttpResponseHandler res) {
		client.get(urlString, params, res);
	}

	// ������������ȡjson�����������
	public static void postJson(String urlString, JsonHttpResponseHandler res) {
		client.post(urlString, res);
	}

	// ����������ȡjson�����������
	public static void postJson(String urlString, RequestParams params, JsonHttpResponseHandler res) {
		client.post(urlString, params, res);
	}

	// ��������ʹ�ã��᷵��byte����
	public static void get(String uString, BinaryHttpResponseHandler bHandler) {
		client.get(uString, bHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
	
	//�ϴ��ļ�
	public static void postImg(String uString,RequestParams params,AsyncHttpResponseHandler res){
		client.post(uString, params, res);
	}
	
	// ȡ������
	public static void cancelRequests(Context context) {
		client.cancelRequests(context, true);
	}
}
