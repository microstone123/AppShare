package com.hyk.download;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.hyk.activity.R;

/**
 * 下载管理状态码
 * 
 * @author Administrator
 * 
 */
public class DownloadConstants {
	/**
	 * 下载等待启动
	 */
	public static final int STATUS_PENDING = 1;

	/**
	 * 下载当前正在运行
	 */
	public static final int STATUS_RUNNING = 2;

	/**
	 * 下载正在等待重试或恢复
	 */
	public static final int STATUS_PAUSED = 4;

	/**
	 * 下载完成
	 */
	public static final int STATUS_SUCCESSFUL = 8;

	/**
	 * 下载失败
	 */
	public static final int STATUS_FAILED = 16;
	
	/**
	 * 没有足够的存储空间。通常，这是因为SD卡已满
	 */
	public static final int ERROR_INSUFFICIENT_SPACE  = 1006 ;
	
	/**
	 * 没有外部存储设备被发现。通常，这是因为未安装在SD卡
	 */
	public static final int  ERROR_DEVICE_NOT_FOUND =  1007 ;
	
	/**
	 * 下载失败
	 */
	public static final int  ERROR_CANNOT_RESUME =  1008 ;
	
	/**
	 * 已经存在
	 */
	public static final int  ERROR_FILE_ALREADY_EXISTS  =  1009 ;
	
	/**
	 * 已经存在
	 */
	public static final int  ERROR_URL  =  404 ;
	
	
	public static void installationApp(Context context,Uri localUri){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(localUri, "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
		try {
			context.startActivity(intent);
		} catch (ActivityNotFoundException ex) {
			Toast.makeText(context, R.string.download_no_application_title, Toast.LENGTH_LONG).show();
		}
	}
}
