package com.hyk.user;

/**
 * @ClassName: QQUserInfo
 * @Description: TODO(QQ����ֵ)
 * @author linhs
 * @date 2014-3-4 ����4:24:25
 */
public class PartyUserInfo {
	/**
	 * id
	 */
	private String id;
	
	/**
	 * QQ�ǳ�
	 */
	private String name;
	
	/**
	 * QQͷ��
	 */
	private String userImg;
	
	/**
	 * �Ա�
	 */
	private String uSex;
	
	/**
	 * ��½����   0:QQ  1:����΢��    2:����
	 */
	private int loginType;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getLoginType() {
		return loginType;
	}
	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getuSex() {
		return uSex;
	}
	public void setuSex(String uSex) {
		this.uSex = uSex;
	}
}
