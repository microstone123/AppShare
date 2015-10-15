package com.hyk.broadcast;

import org.apache.commons.lang.StringUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.hyk.app.ThreadUtils;
import com.hyk.utils.ACache;
import com.hyk.utils.ErrorCode;

/**
 * @ClassName: ConnectionChangeReceiver
 * @Description: TODO(网络连接广播)
 * @author linhs
 * @date 2014-2-18 下午2:55:34
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static Handler handler = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			ACache mAcache = ACache.get(context);
			String isStart = mAcache.getAsString("XmppApplication");
			if ("start".equals(isStart)) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
				android.os.Message msg = new android.os.Message();
				if (activeNetInfo != null) {
					msg.what = ErrorCode.SUCC_NETWORK; // 发送网络连接消息 1001

					String hot_String = mAcache.getAsString("hot_string");
					if (StringUtils.isEmpty(hot_String)) {
						new ThreadUtils(context).start();
					}

				} else {
					msg.what = ErrorCode.ERROR_NETWORK; // 发送网络断开消息 1002

				}
				if (handler != null) {
					handler.sendMessage(msg);
				}
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static Handler getHandler() {
		return handler;
	}

	public static void setHandler(Handler handler) {
		ConnectionChangeReceiver.handler = handler;
	}

}