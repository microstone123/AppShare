package com.hyk.user;

import java.io.Serializable;

public class UserAppInfo implements Serializable {
	/**
	 * app Id
	 */
	public int appId;
	/**
	 * 名称
	 */
	public String appName;
	/**
	 * 包名
	 */
	public String packageName;
	/**
	 * 版本
	 */
	public String appVersion;
	/**
	 * 应用图标路径
	 */
	public String appImage;
	/**
	 * 评分
	 */
	public Float score;
	/**
	 * 类别
	 */
	public String type;
	/**
	 * 下载次数
	 */
	public String downNum;
	/**
	 * 大小(MB)
	 */
	public String appSize;

	/**
	 * 下载地址
	 */
	public String downUrl;
	
	/**
	 * app简介
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
