package com.hyk.user;

import java.util.List;

/**
 * 
 * @ClassName: UserAppList
 * @Description: TODO(��ѯ�ܱߵ��û�APP��Ϣ)
 * @author linhaishi
 * @date 2013-9-18 ����4:05:26
 * 
 */
public class UserList {
	/**
	 * �û�ID
	 */
	private int regId;
	/**
	 * �û�imei
	 */
	private String imei;
	/**
	 * �û���
	 */
	private String userName;
	/**
	 * �û�ͷ��
	 */
	private String headPic;
	/**
	 * �û�ǩ��
	 */
	private String signName;
	/**
	 * �Ƿ�Ϊ����  0��û��ע    1���ѹ�ע
	 */
	private int relation;
	/**
	 * ���룬��λ����
	 */
	private String distance;
	/**
	 * ���û�APP�ĸ���
	 */
	private int appCount;
	/**
	 * �û�APP����,�μ�appList����
	 */
	private List<AppList> apps;
	
	private String time;
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getRegId() {
		return regId;
	}
	public void setRegId(int regId) {
		this.regId = regId;
	}
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
	public String getSignName() {
		return signName;
	}
	public void setSignName(String signName) {
		this.signName = signName;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public int getAppCount() {
		return appCount;
	}
	public void setAppCount(int appCount) {
		this.appCount = appCount;
	}
	public List<AppList> getApps() {
		return apps;
	}
	public void setApps(List<AppList> apps) {
		this.apps = apps;
	}
	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}

	
}
