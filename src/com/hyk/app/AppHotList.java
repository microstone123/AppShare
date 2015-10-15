package com.hyk.app;

/**
 * 
 * @ClassName: UserAppList
 * @Description: TODO(查询周边的用户APP信息)
 * @author linhaishi
 * @date 2013-9-18 下午4:05:26
 * 
 */
public class AppHotList {
	/**
	 * APPID
	 */
	private int appid;
	/**
	 * App名称
	 */
	private String appName;
	/**
	 * App图标路径
	 */
	private String appImage;

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppImage() {
		return appImage;
	}

	public void setAppImage(String appImage) {
		this.appImage = appImage;
	}
}
