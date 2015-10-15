package com.hyk.myapp;

import java.util.ArrayList;
import java.util.List;

import com.hyk.user.UserApp;

public class StaticAppInfo {
	private static StaticAppInfo staticAppInfo = new StaticAppInfo();
	/**
	 * 我的应用
	 */
	private List<LocationApp> myAppList = new ArrayList<LocationApp>();
	/**
	 * 系统应用
	 */
	private List<LocationApp> sysAppList = new ArrayList<LocationApp>();
	/**
	 * 正在运行应用
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
