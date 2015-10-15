package com.hyk.news;


/**
 * 消息列表
 * @author Administrator
 *
 */
public class NewsForInfo {
	/**
	 * 名称
	 */
	private String userName;
	/**
	 * 头像
	 */
	private String headPic;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 注册Id
	 */
	private int regId;
	/**
	 * 时间
	 */
	private String time;
	/**
	 * imei
	 */
	private String imei;
	/**
	 * app图片
	 */
	private String appImage;
	/**
	 * appId
	 */
	private int appId;
	/**
	 * app名称
	 */
	private String appName;
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHeadPic() {
		return headPic;
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getRegId() {
		return regId;
	}
	public void setRegId(int regId) {
		this.regId = regId;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAppImage() {
		return appImage;
	}
	public void setAppImage(String appImage) {
		this.appImage = appImage;
	}
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
}
