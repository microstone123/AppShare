package com.hyk.user;

/**
 * @ClassName: UserApp
 * @Description: TODO(APP��������ϴ��û�����Ӧ����Ϣ)
 * @author linhaishi
 * @date 2013-9-16 ����7:39:52
 * 
 */
public class UserApp {
	/**
	 * app����
	 */
	private String appName;
	/**
	 * app�汾
	 */
	private String appVersion;
	/**
	 * ����
	 */
	private String packageName;

	/**
	 * ��С
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
