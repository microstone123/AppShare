package com.hyk.user;

import java.util.List;

/**
 * 
 * @ClassName: UserAppList
 * @Description: TODO(查询周边的用户APP信息)
 * @author linhaishi
 * @date 2013-9-18 下午4:05:26
 * 
 */
public class UserList {
	/**
	 * 用户ID
	 */
	private int regId;
	/**
	 * 用户imei
	 */
	private String imei;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 用户头像
	 */
	private String headPic;
	/**
	 * 用户签名
	 */
	private String signName;
	/**
	 * 是否为好友  0：没关注    1：已关注
	 */
	private int relation;
	/**
	 * 距离，单位：米
	 */
	private String distance;
	/**
	 * 该用户APP的个数
	 */
	private int appCount;
	/**
	 * 用户APP集合,参见appList对象
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
