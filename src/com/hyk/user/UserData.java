package com.hyk.user;

/**
 * 服务器返回用户信息
 * 
 * @author Administrator
 */
public class UserData {

	/**
	 * 注册ID
	 */
	private String regId;

	/**
	 * 用户名称
	 */
	private String userName;

	/**
	 * 性别
	 */
	private int sex;

	/**
	 * 被点赞的数量
	 */
	private int likedNumber;

	/**
	 * 年龄
	 */
	private int age;

	/**
	 * 头像
	 */
	private String headPic;

	/**
	 * 个性签名
	 */
	private String signName;

	/**
	 * 手机号码
	 */
	private String phoneNumber;

	/**
	 * 
	 */
	private int accountId;

	/**
	 * 
	 */
	private String userKey;

	/**
	 * 是否在线
	 */
	private int isLine;

	/**
	 * 创建时间
	 */
	private String createTime;

	/**
	 * 最后上线时间
	 */
	private String lastUpdateTime;

	/**
	 * 关注个数
	 */
	private String focusHim;

	/**
	 * 粉丝
	 */
	private String focusMe;

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getLikedNumber() {
		return likedNumber;
	}

	public void setLikedNumber(int likedNumber) {
		this.likedNumber = likedNumber;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public int getIsLine() {
		return isLine;
	}

	public void setIsLine(int isLine) {
		this.isLine = isLine;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getFocusHim() {
		return focusHim;
	}

	public void setFocusHim(String focusHim) {
		this.focusHim = focusHim;
	}

	public String getFocusMe() {
		return focusMe;
	}

	public void setFocusMe(String focusMe) {
		this.focusMe = focusMe;
	}

}
