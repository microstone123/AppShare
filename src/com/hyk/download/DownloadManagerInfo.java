package com.hyk.download;

public class DownloadManagerInfo {

	/**
	 * ����Id
	 */
	private int downId;
	
	/**
	 * ���ص�ַ
	 */
	private String appUrl;
	
	/**
	 * app����
	 */
	private String  appName;
	
	/**
	 * �����ܳ���
	 */
	private String  totalSize;
	
	/**
	 * ��ǰ���س���
	 */
	private String  currentSize;
	
	/**
	 * ����״̬
	 */
	private int  statics;
	
	/**
	 * ����ʱ��
	 */
	private String downTime;

	public int getDownId() {
		return downId;
	}

	public String getDownTime() {
		return downTime;
	}

	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}

	public void setDownId(int downId) {
		this.downId = downId;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(String totalSize) {
		this.totalSize = totalSize;
	}

	public String getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(String currentSize) {
		this.currentSize = currentSize;
	}

	public int getStatics() {
		return statics;
	}

	public void setStatics(int statics) {
		this.statics = statics;
	}
	
	
}
