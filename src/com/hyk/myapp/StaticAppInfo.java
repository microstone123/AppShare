package com.hyk.myapp;

import java.util.ArrayList;
import java.util.List;

import com.hyk.user.UserApp;

public class StaticAppInfo {
	private static StaticAppInfo staticAppInfo = new StaticAppInfo();
	/**
	 * �ҵ�Ӧ��
	 */
	private List<LocationApp> myAppList = new ArrayList<LocationApp>();
	/**
	 * ϵͳӦ��
	 */
	private List<LocationApp> sysAppList = new ArrayList<LocationApp>();
	/**
	 * ��������Ӧ��
	 */
	private List<LocationApp> recentlyAppList = new ArrayList<LocationApp>();

	private List<AppInfo> appInfoList = new ArrayList<AppInfo>();
	
	private List<LocationApp> userAppList = new ArrayList<LocationApp>();
	

	public List<LocationApp> getUserAppList() {
		return userAppList;
	}

	public void setUserAppList(List<LocationApp> userAppList) {
		this.userAppList = userAppList;
	}

	public List<AppInfo> getAppInfoList() {
		return appInfoList;
	}

	public void setAppInfoList(List<AppInfo> appInfoList) {
		this.appInfoList = appInfoList;
	}

	public static StaticAppInfo getStaticAppInfo() {
		return staticAppInfo;
	}

	public static void setStaticAppInfo(StaticAppInfo staticAppInfo) {
		StaticAppInfo.staticAppInfo = staticAppInfo;
	}

	public List<LocationApp> getMyAppList() {
		return myAppList;
	}

	public void setMyAppList(List<LocationApp> myAppList) {
		this.myAppList = myAppList;
	}

	public List<LocationApp> getSysAppList() {
		return sysAppList;
	}

	public void setSysAppList(List<LocationApp> sysAppList) {
		this.sysAppList = sysAppList;
	}

	public List<LocationApp> getRecentlyAppList() {
		return recentlyAppList;
	}

	public void setRecentlyAppList(List<LocationApp> recentlyAppList) {
		this.recentlyAppList = recentlyAppList;
	}

}
