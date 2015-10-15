package com.hyk.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * http请求
 * 
 */
public class HttpUtils {
	/**
	 * post请求 ，超时默认30秒, 默认utf-8
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException
	 */
	public String post(String url, Map<String, String> params) throws Exception {
		return this.post(url, params, 10, HTTP.UTF_8);
	}

	/**
	 * post请求, 超时默认30秒
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 *            编码方式
	 * @return
	 * @throws Exception
	 */
	public String post(String url, Map<String, String> params, String charset) throws Exception {
		return this.post(url, params, 30, charset);
	}

	/**
	 * post请求, 默认utf-8
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @return
	 * @throws Exception
	 */
	public String post(String url, Map<String, String> params, int timeout) throws Exception {
		return this.post(url, params, timeout, HTTP.UTF_8);
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @return
	 * @throws IOException
	 */
	public String post(String url, Map<String, String> params, int timeout, String charset) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					formparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, charset);
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);
			HttpResponse resp = httpclient.execute(httppost);
			retVal = EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * 拼接url
	 * 
	 * @param str
	 *            url
	 * @param params
	 *            参数
	 * @return
	 */
	public static String checkUrl(String str, Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		try {
			if (params != null)
				for (Map.Entry<String, String> entry : params.entrySet()) {
					sb.append(entry.getKey().toString() + "=");
					sb.append(URLEncoder.encode(entry.getValue().toString(), "utf-8") + "&");
				}
			str = str + "?" + sb.toString();
			if (str.endsWith("&")) {
				str = str.substring(0, str.length() - 1);
			}
			// str = URLEncoder.encode(str, "utf-8");
			// Log.e("URLEncoder", str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 发送post请求到Google短链接服务器，获取url的短链接
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String postGoogleURLShortener(String url) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", 10000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = url;
		try {
			HttpPost httppost = new HttpPost("https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyABoOMQswr72iix40kj1I42Dk7tBf0kppk");
			httppost.setHeader("Content-Type", "application/json");
			httppost.setEntity(new StringEntity("{\"longUrl\": \"" + url + "\"}"));
			HttpResponse resp = httpclient.execute(httppost);
			String jsonStr = EntityUtils.toString(resp.getEntity(), HTTP.UTF_8);
			if (StringUtils.isNotEmpty(jsonStr)) {
				JSONObject jsonObj = JSONObject.fromObject(jsonStr);
				if (jsonObj.containsKey("id")) {
					retVal = jsonObj.getString("id");
				}
			}
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @param charset
	 *            编码方式
	 * @return
	 * @throws Exception
	 */
	public String get(String url, Map<String, String> params, int timeout, String charset) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		try {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			String paramstr = URLEncodedUtils.format(qparams, charset);
			if (StringUtils.isNotEmpty(paramstr)) {
				url = url + "?" + paramstr;
			}
			HttpGet httpget = new HttpGet(url);

			HttpResponse resp = httpclient.execute(httpget);
			retVal = EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * get请求
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @param charset
	 *            编码方式
	 * @param fNameEndChar
	 *            方法名后结束字符 默认“?”
	 * @return
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> params, int timeout, String charset, String fNameEndChar) throws Exception {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setIntParameter("http.socket.timeout", timeout * 1000);
		httpclient.getParams().setBooleanParameter("http.protocol.expect-continue", false);
		String retVal = "";
		fNameEndChar = (fNameEndChar == null || "".endsWith(fNameEndChar)) ? "?" : fNameEndChar;
		try {
			List<NameValuePair> qparams = new ArrayList<NameValuePair>();
			if (params != null) {
				for (Map.Entry<String, String> param : params.entrySet()) {
					qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				}
			}
			String paramstr = URLEncodedUtils.format(qparams, charset);
			if (StringUtils.isNotEmpty(paramstr)) {
				url = url + fNameEndChar + paramstr;
			}
			HttpGet httpget = new HttpGet(url);

			HttpResponse resp = httpclient.execute(httpget);
			retVal = EntityUtils.toString(resp.getEntity(), charset);
		} catch (IOException e) {
			throw e;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return retVal;
	}

	/**
	 * get请求,超时默认10秒
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 *            编码方式
	 * @return
	 * @throws IOException
	 */
	public String get(String url, Map<String, String> params, String charset) throws Exception {
		return this.get(url, params, 10, charset);
	}

	/**
	 * get请求,超时默认10秒, 默认utf-8
	 * 
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String get(String url, Map<String, String> params) throws Exception {
		return this.get(url, params, 10, HTTP.UTF_8);
	}

	/**
	 * get请求, 默认utf-8
	 * 
	 * @param url
	 * @param params
	 * @param timeout
	 *            超时时间，秒
	 * @return
	 * @throws Exception
	 */
	public String get(String url, Map<String, String> params, int timeout) throws Exception {
		return this.get(url, params, timeout, HTTP.UTF_8);
	}
}