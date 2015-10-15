package com.hyk.myapp;

import android.graphics.drawable.Drawable;

public class AppInfo {
	/**
	 * appͼ��
	 */
	private Drawable appIcon;
	/**
	 * app����
	 */
	private String appName;
	/**
	 * �Ƿ�ΪϵͳӦ��
	 */
	private boolean inRom;
	/**
	 * ����
	 */
	private String packname;
	/**
	 * app�汾
	 */
	private String version;
	/**
	 * �Ƿ�Ϊ�û���װӦ��
	 */
	private boolean userApp;
	/**
	 * Ӧ�ô�С
	 */
	private String appSize;
	/**
	 * ��������
	 */
	private String processName;
	/**
	 * CPU��Ϣ
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
