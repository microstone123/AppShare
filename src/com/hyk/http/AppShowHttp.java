package com.hyk.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.hyk.app.APPCommentList;
import com.hyk.app.AppHotList;
import com.hyk.app.UpdataAppObject;
import com.hyk.center.DynamicInfo;
import com.hyk.myapp.GetPhoneApp;
import com.hyk.myapp.LocationApp;
import com.hyk.myapp.MyappDatabaseUtil;
import com.hyk.myapp.StaticAppInfo;
import com.hyk.resultforjson.ResultStringForComment;
import com.hyk.resultforjson.ResultStringForFrDy;
import com.hyk.resultforjson.ResultStringForHot;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.resultforjson.ResultStringForUpdataList;
import com.hyk.user.PackName;
import com.hyk.user.UserApp;
import com.hyk.user.UserInfo;
import com.hyk.utils.ACache;
import com.hyk.utils.CHString;
import com.hyk.utils.HttpUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.Loger;
import com.hyk.utils.RandomUtil;
import com.hyk.utils.ToastUtil;

public class AppShowHttp {
	private static JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	private static String DISTANCE_INT = "200000000";
	private static String APP_KEY = "abc";
	private static String SIGN_SIGN = "123";
	private static String pageSize = "30";
	private static String currentPage = "1";
	private static String wSize = "6";

	/**
	 * 向服务器上传用户的位置点信息
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	public static int httpForUpLoadLocation(Context context, String lat, String lon) {

		Map<String, String> params = new HashMap<String, String>();
		TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		params.put("imei", telManager.getDeviceId());
		params.put("lat", lat);
		params.put("lng", lon);
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APP_UPLOADLOCATION;
		ResultStringForLoadap resultString = new ResultStringForLoadap();
		HttpUtils http = new HttpUtils();
		int result = 0;
		try {
			String postString = http.post(url, params);
			resultString = binder.fromJson(postString, ResultStringForLoadap.class);
			if (resultString != null) {
				if (resultString.getResult() == 0) {
					ToastUtil.show(context, lat + " " + lon);
				}
			} else {
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @Title: httpForComment
	 * @Description: TODO(根据APPID查询应用评论)
	 * @param @param context
	 * @param @param appId 应用id
	 * @return APPCommentList 返回类型
	 * @throws
	 */
	public static APPCommentList httpForComment(Context context, String appId, int currentPage, int pageSize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("id", appId);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APPID_REVIEW_ADDRESS;
		ResultStringForComment resultStringForComment = new ResultStringForComment();
		APPCommentList aPPCommentList = new APPCommentList();
		int result = -1;
		String postString = "";
		HttpUtils http = new HttpUtils();
		try {
			postString = http.post(url, params);
			resultStringForComment = binder.fromJson(postString, ResultStringForComment.class);
			result = resultStringForComment.getResult();
			if (result == 0) {
				aPPCommentList = resultStringForComment.getObj();
			}
		} catch (Exception e) {
			aPPCommentList = null;
			e.printStackTrace();
		}
		return aPPCommentList;
	}

	/**
	 * 获取热门应用集合
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	public static String httpForHotApp(Context context) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", Httpaddress.PHONE_TYPE);
		params.put("pageSize", pageSize);
		params.put("currentPage", currentPage);
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APPHOT_ADDRESS;
		ResultStringForHot resultString = new ResultStringForHot();
		HttpUtils http = new HttpUtils();
		List<AppHotList> appHotList = new ArrayList<AppHotList>();
		String postString = "";
		try {
			postString = http.post(url, params);
			resultString = binder.fromJson(postString, ResultStringForHot.class);
			if (resultString != null) {
				appHotList = resultString.getObj();
				if (appHotList != null) {
					if (appHotList.size() > 0) {
						ACache mCache = ACache.get(context);
						mCache.put("hot_string", postString);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postString;
	}

	/**
	 * 获取更新应用集合
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	public static void httpForUpdata(Context context) {
		ACache mCache = ACache.get(context);
		Map<String, String> params = new HashMap<String, String>();
		String url = Httpaddress.IP_ADDRESS + Httpaddress.APP_UPDATE;
		params.put("imei", UserInfo.getUserInfo().getIMEI());
		// params.put("imei", "860308020297079");
		params.put("currentPage", "1");
		params.put("pageSize", "30");
		ResultStringForUpdataList resultString = new ResultStringForUpdataList();
		HttpUtils http = new HttpUtils();
		UpdataAppObject appHotList = new UpdataAppObject();
		String postString = "";
		try {
			postString = http.post(url, params);
			resultString = binder.fromJson(postString, ResultStringForUpdataList.class);
			if (resultString != null) {
				appHotList = resultString.getObj();
				if (appHotList != null) {
					mCache.put("manage_updata_string", postString);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 本地应用上传
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	public static String httpForUserAppInfo(Context context, boolean isData) {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.UPLOADAPPINFO_ADDRESS;
		List<UserApp> appInfoList = new ArrayList<UserApp>();
		List<LocationApp> locationAppList = new ArrayList<LocationApp>();
		locationAppList = GetPhoneApp.myLoadApps(context);
		String postString = "";
		if (locationAppList != null) {
			for (LocationApp locationApp : locationAppList) {
				UserApp userApp = new UserApp();
				userApp.setAppName(locationApp.getName());
				userApp.setAppSize(locationApp.getAppSize());
				userApp.setPackageName(locationApp.getPackName());
				userApp.setAppVersion(locationApp.getAppVersion());
				appInfoList.add(userApp);
			}
			
			Map<String, String> params = new HashMap<String, String>();
			TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			params.put("imei", telManager.getDeviceId());
			params.put("userApps", binder.toJson(appInfoList));
			params.put("type", String.valueOf(Httpaddress.PHONE_TYPE));
			ResultStringForLoadap resultString = new ResultStringForLoadap();
			HttpUtils http = new HttpUtils();
			try {
				Loger.i("本地应用上传", url+" "+params.get("imei") +" "+params.get("userApps")+" "+params.get("type"));
				postString = http.post(url, params);
				resultString = binder.fromJson(postString, ResultStringForLoadap.class);
				if (resultString != null) {
					if (resultString.getResult() != 0) {
						httpForUserAppInfo(context, true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				httpForUserAppInfo(context, true);
			}
		}
		return postString;
	}

	/**
	 * 正在使用应用上传
	 * 
	 * @param params
	 * @param url
	 * @return
	 */
	public static void httpForUploadUserApp(Context context, String regId) {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.UPLOAD_USERAPP;
		List<LocationApp> appInfoList = new ArrayList<LocationApp>();
		MyappDatabaseUtil myappDatabaseUtil = new MyappDatabaseUtil(context);

		appInfoList = myappDatabaseUtil.queryForTime(1);
		if (appInfoList != null) {
			if (appInfoList.size() > 0) {
				int[] useNum = RandomUtil.getExchangeCode(appInfoList.size(), appInfoList.size() / 2);
				List<PackName> packageName = new ArrayList<PackName>();
				for (int i = 0; i < useNum.length; i++) {
					PackName p = new PackName();
					p.setPackageName(appInfoList.get(useNum[i]).getPackName());
					packageName.add(p);
				}
				Map<String, String> params = new HashMap<String, String>();
				params.put("regId", regId);
				params.put("userApps", binder.toJson(packageName));
				ResultStringForLoadap resultString = new ResultStringForLoadap();
				HttpUtils http = new HttpUtils();
				String postString = "";
				try {
					Loger.i("正在使用应用上传",params.get("regId")+" "+params.get("userApps"));
					postString = http.post(url, params);
					resultString = binder.fromJson(postString, ResultStringForLoadap.class);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 请求我的动态数据
	 */
	public static List<DynamicInfo> httpForFrienddynamic(Context context, String redId, int currentPage, int pageSize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", redId);
		params.put("comCurrentPage", "1");
		params.put("comPageSize", "30");
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USERNEWS_OTHER;
		ResultStringForFrDy resultString = new ResultStringForFrDy();
		HttpUtils http = new HttpUtils();
		List<DynamicInfo> appHotList = new ArrayList<DynamicInfo>();
		String postString = "";
		try {
			postString = http.post(url, params);
			resultString = binder.fromJson(postString, ResultStringForFrDy.class);
			if (resultString != null) {
				appHotList = resultString.getObj().getNewslist();
				if (appHotList != null) {
					if (appHotList.size() > 0) {
						ACache mCache = ACache.get(context);
						mCache.put("friend_data_string", postString);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return appHotList;
	}

	/**
	 * 请求用户app
	 */
	public static String httpForUserApp(Context context, int currentPage, int pageSize) {
		String postString = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			ACache mACache = ACache.get(context);
			String imei = mACache.getAsString("user_imei");
			params.put("imei", imei);
			params.put("currentPage", String.valueOf(currentPage));
			params.put("pageSize", String.valueOf(pageSize));
			String url = Httpaddress.IP_ADDRESS + Httpaddress.APP_USERAPP;
			HttpUtils http = new HttpUtils();
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 请求我的动态数据
	 */
	public static String httpFormydynamic(Context context, String redId, int currentPage, int pageSize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", redId);
		params.put("comCurrentPage", "1");
		params.put("comPageSize", "15");
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String url = Httpaddress.IP_ADDRESS + Httpaddress.GET_MYDYNAMIC;
		HttpUtils http = new HttpUtils();
		String postString = "";
		try {
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 请求附近应用数据
	 */
	public static String httpForNearestUser(Context context, String redId, int currentPage, int pageSize) {
		Map<String, String> params = new HashMap<String, String>();
		ACache mACache = ACache.get(context);
		String coordinates = mACache.getAsString("location_key");
		if (StringUtils.isNotEmpty(coordinates)) {
			String[] latlnt = coordinates.split(",");
			params.put("lat", latlnt[0]);
			params.put("lon", latlnt[1]);
		} else {
			params.put("lat", "22.24");
			params.put("lon", "113.53");
		}
		params.put("distance", CHString.DISTANCE_INT);
		params.put("regId", redId);
		params.put("type", Httpaddress.PHONE_TYPE);
		params.put("wSize", CHString.WSIZE);
		params.put("imei", UserInfo.getUserInfo().getIMEI());
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String url = Httpaddress.IP_ADDRESS + Httpaddress.NEARESTUSER_ADDRESS;
		HttpUtils http = new HttpUtils();
		String postString = "";
		try {
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 用户接收的最新的消息列表
	 */
	public static String httpForMessagelist(Context context, String redId, int currentPage, int pageSize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", redId);
		params.put("currentPage", String.valueOf(currentPage));
		params.put("pageSize", String.valueOf(pageSize));
		String url = Httpaddress.IP_ADDRESS + Httpaddress.NEWS_MESSAGELIST;
		HttpUtils http = new HttpUtils();
		String postString = "";
		try {
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 查询用户详情
	 */
	public static String httpForUserInfo(Context context, String redId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("regId", redId);
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_INFO;
		HttpUtils http = new HttpUtils();
		String postString = "";
		try {
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 关注(取消关注和关注)
	 */
	public static String httpForAttention(Context context, String redId, String friendId, int mark) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("myId", redId);
		params.put("friendId", friendId);
		params.put("mark", String.valueOf(mark));
		String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_FIREND;
		HttpUtils http = new HttpUtils();
		String postString = "";
		try {
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 请求用户app
	 */
	public static String httpForFriend(Context context, int currentPage, int pageSize, String regId, int relation) {
		String postString = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("regId", regId);
			params.put("currentPage", String.valueOf(currentPage));
			params.put("pageSize", String.valueOf(pageSize));
			params.put("mark", relation + "");
			String url = Httpaddress.IP_ADDRESS + Httpaddress.USER_APPLIST;
			HttpUtils http = new HttpUtils();
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 请求我的动态数据
	 */
	public static String httpFormydynamic(Context context, int currentPage, int pageSize, String regId) {
		String postString = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("regId", regId);
			params.put("comCurrentPage", "1");
			params.put("comPageSize", "15");
			params.put("currentPage", String.valueOf(currentPage));
			params.put("pageSize", String.valueOf(pageSize));
			String url = Httpaddress.IP_ADDRESS + Httpaddress.GET_MYDYNAMIC;
			// String url = "http://192.168.0.32:8080/" +
			// Httpaddress.GET_MYDYNAMIC;
			HttpUtils http = new HttpUtils();
			postString = http.post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return postString;
	}

	/**
	 * 发表动态
	 */
	public static String httpForSetdynamic(Context context, String content, String redId, String send) {
		String postString = "";
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("content", content);
			if(StringUtils.isNotEmpty(send)){
				params.put("size", String.valueOf(send.length()));
			}else{
				params.put("size", "0");
			}
			params.put("regId", redId);
			params.put("images", send);
			String url = Httpaddress.IP_ADDRESS + Httpaddress.SEND_NEWS;
			Loger.i("发表动态", url+" "+params.get("content")+" "+params.get("regId")+" "+params.get("images")+" "+params.get("size"));
			HttpUtils http = new HttpUtils();
			postString = http.post(url, params);
			Log.e("postString", postString);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (OutOfMemoryError e) {
			Log.e("OutOfMemoryError", "OutOfMemoryError");
			return e.getMessage();
		}
		return postString;
	}

}
