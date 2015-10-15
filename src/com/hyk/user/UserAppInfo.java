package com.hyk.user;

import java.io.Serializable;

public class UserAppInfo implements Serializable {
	/**
	 * app Id
	 */
	public int appId;
	/**
	 * ����
	 */
	public String appName;
	/**
	 * ����
	 */
	public String packageName;
	/**
	 * �汾
	 */
	public String appVersion;
	/**
	 * Ӧ��ͼ��·��
	 */
	public String appImage;
	/**
	 * ����
	 */
	public Float score;
	/**
	 * ���
	 */
	public String type;
	/**
	 * ���ش���
	 */
	public String downNum;
	/**
	 * ��С(MB)
	 */
	public String appSize;

	/**
	 * ���ص�ַ
	 */
	public String downUrl;
	
	/**
	 * app���
	 */
	public String introduce;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppImage() {
		return appImage;
	}

	public void setAppImage(String appImage) {
		this.appImage = appImage;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDownNum() {
		return downNum;
	}

	public void setDownNum(String downNum) {
		this.downNum = downNum;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	

}
