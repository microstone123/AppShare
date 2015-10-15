package com.hyk.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * class name：SIMCardInfo
 * 
 * class description：读取Sim卡信�?
 * 
 * PS�?必须在加入各种权�?
 * 
 * Date:2013-8-1
 * 
 * 
 * @version 1.00
 * @author CODYY)linhs
 */
public class SIMCardInfo {
	/**
	 * TelephonyManager提供设备上获取�?讯服务信息的入口�?应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息�?
	 * 应用程序也可以注册一个监听器到电话收状�?的变化�?不需要直接实例化这个�?
	 * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例�?
	 */
	private TelephonyManager telephonyManager;
	/**
	 * 国际移动用户识别�?
	 */
	private String IMSI;

	public SIMCardInfo(Context context) {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public String getIMSI(Context context) {
		String SIMCard = null;
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		 SIMCard =telephonyManager.getSubscriberId();
		 return SIMCard;
	}

	/**
	 * Role:获取当前设置的电话号�?
	 * 
	 * Date:2013-8-1
	 * 
	 * @author CODYY)linhs
	 */
	public String getNativePhoneNumber() {
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}

	/**
	 * Role:获取手机MAC
	 * 
	 * Date:2013-8-1
	 * 
	 * @author CODYY)linhs
	 */
	public String getMacAddress(Context context) {
            String result = "";
            WifiManager wifiManager = (WifiManager) context
    				.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            result = wifiInfo.getMacAddress();
            // Log.i(TAG, "macAdd:" + result);
            return result;
    }
	
	/**
	 * Role:手机型号
	 * 
	 * Date:2013-8-1
	 * 
	 * @author CODYY)linhs
	 */
	public String getInfoTYPE(Context context) {
//		TelephonyManager myTelephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String mtype = android.os.Build.MODEL; // 手机型号
		return mtype;
		}
	
	public String getInfoIMEI(Context context) {
		TelephonyManager myTelephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
		String imei = myTelephonyManager.getDeviceId();
		return imei;
		}
	
	
	/**
	 * Role:Telecom service providers获取手机服务商信�?
	 * 
	 * �?��加入权限<uses-permission
	 * Android:name="android.permission.READ_PHONE_STATE"/>
	 * 
	 * Date:2013-8-1
	 * 
	 * 
	 * @author CODYY)linhs
	 */
	public String getProvidersName() {
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号神马�?
		IMSI = telephonyManager.getSubscriberId();
		// IMSI号前�?�?60是国家，紧接�?���?�?0 02是中国移动，01是中国联通，03是中国电信�?
		System.out.println(IMSI);
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联�?";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		}
		return ProvidersName;
	}
}