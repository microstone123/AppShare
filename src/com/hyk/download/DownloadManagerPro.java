package com.hyk.download;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import com.hyk.providers.DownloadManager;
import com.hyk.providers.DownloadManager.Request;
import com.hyk.providers.downloads.Downloads;
import com.hyk.utils.FileHelper;

@SuppressLint("InlinedApi")
public class DownloadManagerPro {

	public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");
	/** represents downloaded file above api 11 **/
	public static final String COLUMN_LOCAL_FILENAME = "local_filename";
	/** represents downloaded file below api 11 **/
	public static final String COLUMN_LOCAL_URI = "local_uri";

	public static final String METHOD_NAME_PAUSE_DOWNLOAD = "pauseDownload";
	public static final String METHOD_NAME_REMOVE_DOWNLOAD = "remove";
	public static final String METHOD_NAME_RESUME_DOWNLOAD = "resumeDownload";

	private static boolean isInitPauseDownload = false;
	private static boolean isInitResumeDownload = false;

	private static boolean isInitRemoveDownload = false;

	private static Method pauseDownload = null;
	private static Method resumeDownload = null;
	private static Method removeDownload = null;
	private DownloadManager downloadManager;
	private Context context;
	private DownloadsChangeObserver mDownloadObserver;
	private DownloadManagerDatabaseUtil downloadManagerDatabaseUtil;
	private FileHelper fileHelper;

	public DownloadManagerPro(Context context) {
		this.context = context;
		this.downloadManager = new DownloadManager(context.getContentResolver(), context.getPackageName());
		this.downloadManager.setAccessAllDownloads(false);
		this.downloadManagerDatabaseUtil = new DownloadManagerDatabaseUtil(context);
		this.fileHelper = new FileHelper(context);
	}

	public int startDown(String url, Handler handler, int handleNum, String appName) {
		Uri srcUri = Uri.parse(url);
		DownloadManager.Request request = new Request(srcUri);
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, appName + ".apk");
		// request.setDescription("Just for test");
		// request.
		request.setShowRunningNotification(false);
		int downId = (int) downloadManager.enqueue(request);

//		startDownloadObserver(handler, downId, handleNum);
		return downId;
	}

	/**
	 * ��ȡ����״̬
	 * 
	 * @param downloadId
	 **            1:���صȴ����� 2:���ص�ǰ�������� 4: �������ڵȴ����Ի�ָ� 8:������� 16:����ʧ��
	 * @return
	 */
	public int getStatusById(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_STATUS);
	}

//	/*
//	 * �������ݿ����
//	 */
//	public void startDownloadObserver(Handler handler, int downId, int handleNum) {
//		if (mDownloadObserver != null) {
//			context.getContentResolver().unregisterContentObserver(mDownloadObserver);
//		}
//		mDownloadObserver = new DownloadsChangeObserver(handler, this, downId, handleNum);
//		context.getContentResolver().registerContentObserver(Downloads.CONTENT_URI, true, mDownloadObserver);
//	}
//
//	/*
//	 * �ر����ݿ����
//	 */
//	public void unRegisterContentObserver() {
//		if (mDownloadObserver != null) {
//			context.getContentResolver().unregisterContentObserver(mDownloadObserver);
//		}
//	}

	/**
	 * ��ȡ���ص��ֽڣ��ֽ�����
	 * 
	 * @param downloadId
	 * @return a int array with two elements
	 *         <ul>
	 *         <li>result[0] represents downloaded bytes, This will initially be
	 *         -1.</li>
	 *         <li>result[1] represents total bytes, This will initially be -1.</li>
	 *         </ul>
	 */
	public int[] getDownloadBytes(long downloadId) {
		int[] bytesAndStatus = getBytesAndStatus(downloadId);
		return new int[] { bytesAndStatus[0], bytesAndStatus[1] };
	}

	/**
	 * ��ȡ���ص��ֽڣ��ֽ�����������״̬
	 * 
	 * @param downloadId
	 * @return a int array with three elements
	 *         <ul>
	 *         <li>result[0] represents downloaded bytes, This will initially be
	 *         -1.</li>
	 *         <li>result[1] represents total bytes, This will initially be -1.</li>
	 *         <li>result[2] represents download status, This will initially be
	 *         0.</li>
	 *         </ul>
	 */
	public int[] getBytesAndStatus(long downloadId) {
		int[] bytesAndStatus = new int[] { -1, -1, 0 };
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				bytesAndStatus[0] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
				bytesAndStatus[1] = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
				bytesAndStatus[2] = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return bytesAndStatus;
	}

	/**
	 * ��ȡ�����ļ�����·��
	 * 
	 * @param downloadId
	 *            ����Id
	 * @return
	 */
	public Uri getFileUriString(int downloadId, String appName) {
		DownloadManager.Query query = new DownloadManager.Query().setFilterById((long) downloadId);
		Cursor cursor = null;
		String filePath = "";
		try {
			cursor = downloadManager.query(query);
			if (cursor != null && cursor.moveToFirst()) {
				int mLocalUriColumnId = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
				filePath = cursor.getString(mLocalUriColumnId);
				if (StringUtils.isEmpty(filePath)) {
					// cursor.getString(cursor.getColumnIndex("hint"));
					filePath = "file:///storage/sdcard0/Download/" + appName + ".apk";
				}
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		if (StringUtils.isNotEmpty(filePath)) {
			return Uri.parse(filePath);
		} else {
			return null;
		}

	}

	/**
	 * ��ͣ����
	 * 
	 * @param ids
	 *            the IDs of the downloads to be paused
	 * @return the number of downloads actually paused, -1 if exception or
	 *         method not exist
	 */
	public int pauseDownload(int totalSize, int currentSize, long... ids) {
		if(totalSize==0){
			downloadManagerDatabaseUtil.update(4, (int) ids[0]);
		}else{
			downloadManagerDatabaseUtil.update(totalSize, currentSize, 4, (int) ids[0]);
		}
		return downloadManager.pauseDownload(ids);
	}

	public int restartDownload(Handler handler, int handleNum,long... ids) {
//		startDownloadObserver(handler, (int)ids[0], handleNum);
		return downloadManager.restartDownload(ids);
	}
	/**
	 * ȡ������
	 * 
	 * @param ids
	 *            the IDs of the downloads to be paused
	 * @return the number of downloads actually paused, -1 if exception or
	 *         method not exist
	 */
	public int removeDownload(long... ids) {
		initRemoveMethod();
		if (removeDownload == null) {
			return -1;
		}

		try {
			return ((Integer) removeDownload.invoke(downloadManager, ids)).intValue();
		} catch (Exception e) {
			/**
			 * accept all exception, include ClassNotFoundException,
			 * NoSuchMethodException, InvocationTargetException,
			 * NullPointException
			 */
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * �ָ�����
	 * 
	 * @param ids
	 *            the IDs of the downloads to be resumed
	 * @return the number of downloads actually resumed, -1 if exception or
	 *         method not exist
	 */
	public int resumeDownload(Handler handler, int downId, int handleNum) {
//		startDownloadObserver(handler, downId, handleNum);
		return downloadManager.resumeDownload(downId);
	}

	/**
	 * whether exist pauseDownload and resumeDownload method in
	 * {@link DownloadManager}
	 * 
	 * @return
	 */
	public static boolean isExistPauseAndResumeMethod() {
		initPauseMethod();
		initResumeMethod();
		initRemoveMethod();
		return pauseDownload != null && resumeDownload != null;
	}

	private static void initPauseMethod() {
		if (isInitPauseDownload) {
			return;
		}

		isInitPauseDownload = true;
		try {
			pauseDownload = DownloadManager.class.getMethod(METHOD_NAME_PAUSE_DOWNLOAD, long[].class);
		} catch (Exception e) {
			// accept all exception
			e.printStackTrace();
		}
	}

	private static void initRemoveMethod() {
		if (isInitRemoveDownload) {
			return;
		}

		isInitRemoveDownload = true;
		try {
			removeDownload = DownloadManager.class.getMethod(METHOD_NAME_REMOVE_DOWNLOAD, long[].class);
		} catch (Exception e) {
			// accept all exception
			e.printStackTrace();
		}
	}

	private static void initResumeMethod() {
		if (isInitResumeDownload) {
			return;
		}

		isInitResumeDownload = true;
		try {
			resumeDownload = DownloadManager.class.getMethod(METHOD_NAME_RESUME_DOWNLOAD, long[].class);
		} catch (Exception e) {
			// accept all exception
			e.printStackTrace();
		}
	}

	/**
	 * ��������ļ���
	 * 
	 * @param downloadId
	 * @return
	 */
	public String getFileName(long downloadId) {
		return getString(downloadId, (Build.VERSION.SDK_INT < 11 ? COLUMN_LOCAL_URI : COLUMN_LOCAL_FILENAME));
	}

	/**
	 * �������URI
	 * 
	 * @param downloadId
	 * @return
	 */
	public String getUri(long downloadId) {
		return getString(downloadId, DownloadManager.COLUMN_URI);
	}

	/**
	 * ��ȡ����ʧ�ܻ���ͣ��ԭ��
	 * 
	 * @param downloadId
	 * @return <ul>
	 *         <li>if status of downloadId is
	 *         {@link DownloadManager#STATUS_PAUSED}, return
	 *         {@link #getPausedReason(long)}</li>
	 *         <li>if status of downloadId is
	 *         {@link DownloadManager#STATUS_FAILED}, return
	 *         {@link #getErrorCode(long)}</li>
	 *         <li>if status of downloadId is neither
	 *         {@link DownloadManager#STATUS_PAUSED} nor
	 *         {@link DownloadManager#STATUS_FAILED}, return 0</li>
	 *         </ul>
	 */
	public int getReason(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_REASON);
	}

	/**
	 * �õ���ͣ��ԭ��
	 * 
	 * @param downloadId
	 * @return <ul>
	 *         <li>if status of downloadId is
	 *         {@link DownloadManager#STATUS_PAUSED}, return one of
	 *         {@link DownloadManager#PAUSED_WAITING_TO_RETRY}<br/>
	 *         {@link DownloadManager#PAUSED_WAITING_FOR_NETWORK}<br/>
	 *         {@link DownloadManager#PAUSED_QUEUED_FOR_WIFI}<br/>
	 *         {@link DownloadManager#PAUSED_UNKNOWN}</li>
	 *         <li>else return {@link DownloadManager#PAUSED_UNKNOWN}</li>
	 *         </ul>
	 */
	public int getPausedReason(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_REASON);
	}

	/**
	 * �õ�ʧ�ܵĴ������
	 * 
	 * @param downloadId
	 * @return one of {@link DownloadManager#ERROR_*}
	 */
	public int getErrorCode(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_REASON);
	}

	public static class RequestPro extends DownloadManager.Request {

		public static final String METHOD_NAME_SET_NOTI_CLASS = "setNotiClass";
		public static final String METHOD_NAME_SET_NOTI_EXTRAS = "setNotiExtras";

		private static boolean isInitNotiClass = false;
		private static boolean isInitNotiExtras = false;

		private static Method setNotiClass = null;
		private static Method setNotiExtras = null;

		/**
		 * @param uri
		 *            the HTTP URI to download.
		 */
		public RequestPro(Uri uri) {
			super(uri);
		}

		/**
		 * set noti class, only init once
		 * 
		 * @param className
		 *            full class name
		 */
		public void setNotiClass(String className) {
			synchronized (this) {

				if (!isInitNotiClass) {
					isInitNotiClass = true;
					try {
						setNotiClass = Request.class.getMethod(METHOD_NAME_SET_NOTI_CLASS, CharSequence.class);
					} catch (Exception e) {
						// accept all exception
						e.printStackTrace();
					}
				}
			}

			if (setNotiClass != null) {
				try {
					setNotiClass.invoke(this, className);
				} catch (Exception e) {
					/**
					 * accept all exception, include ClassNotFoundException,
					 * NoSuchMethodException, InvocationTargetException,
					 * NullPointException
					 */
					e.printStackTrace();
				}
			}
		}

		/**
		 * set noti extras, only init once
		 * 
		 * @param extras
		 */
		public void setNotiExtras(String extras) {
			synchronized (this) {

				if (!isInitNotiExtras) {
					isInitNotiExtras = true;
					try {
						setNotiExtras = Request.class.getMethod(METHOD_NAME_SET_NOTI_EXTRAS, CharSequence.class);
					} catch (Exception e) {
						// accept all exception
						e.printStackTrace();
					}
				}
			}

			if (setNotiExtras != null) {
				try {
					setNotiExtras.invoke(this, extras);
				} catch (Exception e) {
					/**
					 * accept all exception, include ClassNotFoundException,
					 * NoSuchMethodException, InvocationTargetException,
					 * NullPointException
					 */
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * get string column
	 * 
	 * @param downloadId
	 * @param columnName
	 * @return
	 */
	private String getString(long downloadId, String columnName) {
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		String result = null;
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				if (c.getColumnIndex(columnName) > -1) {
					result = c.getString(c.getColumnIndex(columnName));
				}
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}

	/**
	 * get int column
	 * 
	 * @param downloadId
	 * @param columnName
	 * @return
	 */
	private int getInt(long downloadId, String columnName) {
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		int result = -1;
		Cursor c = null;
		try {
			c = downloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				result = c.getInt(c.getColumnIndex(columnName));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}
}
