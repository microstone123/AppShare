package com.hyk.app;

/**
 * 
 * @ClassName: UserAppList
 * @Description: TODO(��ѯ�ܱߵ��û�APP��Ϣ)
 * @author linhaishi
 * @date 2013-9-18 ����4:05:26
 * 
 */
public class AppHotList {
	/**
	 * APPID
	 */
	private int appid;
	/**
	 * App����
	 */
	private String appName;
	/**
	 * Appͼ��·��
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
