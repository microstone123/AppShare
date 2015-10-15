package com.hyk.myapp;

import android.graphics.drawable.Drawable;

public class AppInfo {
	/**
	 * app图标
	 */
	private Drawable appIcon;
	/**
	 * app名称
	 */
	private String appName;
	/**
	 * 是否为系统应用
	 */
	private boolean inRom;
	/**
	 * 包名
	 */
	private String packname;
	/**
	 * app版本
	 */
	private String version;
	/**
	 * 是否为用户安装应用
	 */
	private boolean userApp;
	/**
	 * 应用大小
	 */
	private String appSize;
	/**
	 * 进程名称
	 */
	private String processName;
	/**
	 * CPU信息
	 */
	private String cpuInfo;
	private boolean useNetwork;
	private boolean useGPS;
	private boolean useContact;

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public boolean isInRom() {
		return inRom;
	}

	public void setInRom(boolean inRom) {
		this.inRom = inRom;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getCpuInfo() {
		return cpuInfo;
	}

	public void setCpuInfo(String cpuInfo) {
		this.cpuInfo = cpuInfo;
	}

	public boolean isUseNetwork() {
		return useNetwork;
	}

	public void setUseNetwork(boolean useNetwork) {
		this.useNetwork = useNetwork;
	}

	public boolean isUseGPS() {
		return useGPS;
	}

	public void setUseGPS(boolean useGPS) {
		this.useGPS = useGPS;
	}

	public boolean isUseContact() {
		return useContact;
	}

	public void setUseContact(boolean useContact) {
		this.useContact = useContact;
	}

}
