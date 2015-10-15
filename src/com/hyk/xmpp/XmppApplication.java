package com.hyk.xmpp;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.hyk.activity.WelcomeActivity;
import com.hyk.chat.ChatDatabaseUtil;
import com.hyk.download.DownloadsChangeObserver;
import com.hyk.utils.AppManager;
import com.hyk.utils.Loger;
import com.hyk.utils.Utils;
import com.hyk.xmpp.listener.MessageInterceptor;
import com.hyk.xmpp.listener.MessageListener;
import com.hyk.xmpp.model.OneFridenMessages;

public class XmppApplication extends Application implements Thread.UncaughtExceptionHandler {
	public static XmppApplication sInstance;
	public static ChatDatabaseUtil chatDatabaseUtil;
	public static SharedPreferences sharedPreferences;
	public static SharedPreferences sharedPreferenceNews;
	public static String user;
	public static final String XMPP_UP_MESSAGE_ACTION = "com.tarena.xmpp.chat.up.message.action";
	// �������к��ѵ�����������Ϣ
	public static ConcurrentHashMap<String, OneFridenMessages> AllFriendsMessageMapData;
	public LocationClient mLocationClient = null;
	public GeofenceClient mGeofenceClient;
	public Handler handler;
	public static final int TIMER_CODE = 1002;
	public Vibrator mVibrator01;
	public static String TAG = "LocTestDemo";
	// private ACache mAcache;
	public static DownloadsChangeObserver downloadsChangeObserver;
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue mRequestQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			Loger.i("qqaa XmppApplication", "��������");
//			Log.e("XmppApplication", "onChat");
			
//			MessageListener.onChat=1;
//			Thread.setDefaultUncaughtExceptionHandler(this);
			sInstance = this;
//			if (mRequestQueue == null) {
//				mRequestQueue = Volley.newRequestQueue(getApplicationContext());
//			}
//			// ��ʼ��imageLoader
//			imageLoader = new ImageLoader(mRequestQueue, new ImageCache() {
//				public void putBitmap(String url, Bitmap bitmap) {
//					// ���سɹ�������ڴ滺����
//					imagesCache.put(url, bitmap);
//				}
//
//				public Bitmap getBitmap(String url) {
//					Bitmap bitmap = null;
//					try {
//
//						// ���ڴ滺���л�ȡͼƬ
//						bitmap = imagesCache.get(url);
//						// bitmapList.add(bitmap);
//					} catch (OutOfMemoryError e) {
//						// TODO: handle exception
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//					return bitmap;
//				}
//			});
//
//			XmppConnection.setContext(getApplicationContext());
//			downloadsChangeObserver = new DownloadsChangeObserver(getApplicationContext());
//			getContentResolver().registerContentObserver(Downloads.CONTENT_URI, true, downloadsChangeObserver);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
//		Loger.e("uncaughtException",arg1.getMessage());
		AppManager.getAppManager().finishAllActivity();
		System.exit(0);
		Intent intent = new Intent(this, WelcomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	public void setConnten(Context context) {
		try {
			XmppConnection.openConnection();

			AllFriendsMessageMapData = new ConcurrentHashMap<String, OneFridenMessages>();
			chatDatabaseUtil = new ChatDatabaseUtil(context);
			// ����Ϣ����
			sharedPreferences = context.getSharedPreferences("newMsgCount", Context.MODE_PRIVATE);
			sharedPreferenceNews = context.getSharedPreferences("newsMsg", Context.MODE_PRIVATE);

			// ��Ϣ��������
			MessageInterceptor mMessageInterceptor = new MessageInterceptor();
			XmppConnection.getConnection().addPacketInterceptor(mMessageInterceptor,
					new PacketTypeFilter(Message.class));
			// ��Ϣ��������
			MessageListener mMessageListener = new MessageListener(context);
			XmppConnection.getConnection().addPacketListener(mMessageListener, new PacketTypeFilter(Message.class));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static XmppApplication getsInstance() {
		return sInstance;
	}

	/**
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	/**
	 * �������
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		Loger.i(tag, req.getUrl());
		cancelPendingRequests(tag);
		getRequestQueue().add(req);
	}

	/**
	 * ʹ��Ĭ�ϱ���������
	 * 
	 * @param req
	 * @param tag
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		// set the default tag if tag is empty
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	/**
	 * ȡ������
	 * 
	 * @param tag
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	// ���õ�����ڴ滺��
	private int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 15; // 5MB
	// com.android.volley.toolbox���µ�ͼƬ������
	public ImageLoader imageLoader;

	// V4���е�LruCache��
	// ���ͼƬ��������
	public HashSet<SoftReference<Bitmap>> mReusableBitmaps = new HashSet<SoftReference<Bitmap>>();

	public LruCache<String, Bitmap> imagesCache = new LruCache<String, Bitmap>(DEFAULT_MEM_CACHE_SIZE) {
		@Override
		protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
			// TODO Auto-generated method stub
			// ���������õ�����ڴ����Ƶ�ͼƬ����������ϵͳ���Զ�����
			mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue));
			oldValue.recycle();
		}

		// ��д�����������ͼƬ�Ĵ�С
		@Override
		protected int sizeOf(String key, Bitmap value) {
			// TODO Auto-generated method stub
			final int bitmapSize = getBitmapSize(value) / 1024;
			// value.recycle();
			return bitmapSize == 0 ? 1 : bitmapSize;
		}
	};

	public int getBitmapSize(Bitmap value) {
		// Bitmap bitmap = value.getBitmap();
		if (Utils.hasHoneycombMR1()) {
			return value.getByteCount();
		}
		return value.getRowBytes() * value.getHeight();
	}
}
