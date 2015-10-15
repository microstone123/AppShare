package com.hyk.user;

/**
 * @ClassName: UserApp
 * @Description: TODO(APP向服务器上传用户本地应用信息)
 * @author linhaishi
 * @date 2013-9-16 下午7:39:52
 * 
 */
public class UserApp {
	/**
	 * app名称
	 */
	private String appName;
	/**
	 * app版本
	 */
	private String appVersion;
	/**
	 * 包名
	 */
	private String packageName;

	/**
	 * 大小
	 */
	private String appSize;

	private int isStart;

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	public int getIsStart() {
		return isStart;
	}

	public void setIsStart(int isStart) {
		this.isStart = isStart;
	}

}
