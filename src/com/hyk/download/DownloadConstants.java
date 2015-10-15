package com.hyk.download;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.hyk.activity.R;

/**
 * ���ع���״̬��
 * 
 * @author Administrator
 * 
 */
public class DownloadConstants {
	/**
	 * ���صȴ�����
	 */
	public static final int STATUS_PENDING = 1;

	/**
	 * ���ص�ǰ��������
	 */
	public static final int STATUS_RUNNING = 2;

	/**
	 * �������ڵȴ����Ի�ָ�
	 */
	public static final int STATUS_PAUSED = 4;

	/**
	 * �������
	 */
	public static final int STATUS_SUCCESSFUL = 8;

	/**
	 * ����ʧ��
	 */
	public static final int STATUS_FAILED = 16;
	
	/**
	 * û���㹻�Ĵ洢�ռ䡣ͨ����������ΪSD������
	 */
	public static final int ERROR_INSUFFICIENT_SPACE  = 1006 ;
	
	/**
	 * û���ⲿ�洢�豸�����֡�ͨ����������Ϊδ��װ��SD��
	 */
	public static final int  ERROR_DEVICE_NOT_FOUND =  1007 ;
	
	/**
	 * ����ʧ��
	 */
	public static final int  ERROR_CANNOT_RESUME =  1008 ;
	
	/**
	 * �Ѿ�����
	 */
	public static final int  ERROR_FILE_ALREADY_EXISTS  =  1009 ;
	
	/**
	 * �Ѿ�����
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
