package com.hyk.resultforjson;

/**
 * @ClassName: LoginResult
 * @Description: TODO(��½����json��obj��ʽ)
 * @author linhaishi
 * @date 2013-9-16 ����5:50:32
 * 
 */
public class LoginResult {
	/**
	 * �û�Id,�û��û���ƽ̨��Ψһʶ����
	 */
	private String regId;
	/**
	 * �û�����
	 */
	private String userName;
	/**
	 * �绰����
	 */
	private String phoneNum;
	/**
	 * 
	 */
	private int friendsNum;
	/**
	 * ͷ��
	 */
	private String headPic;
	/**
	 * ����ǩ��
	 */
	private String signName;
	/**
	 * 
	 */
	private int meNum;
	
	private String focusHim;
	
	private String focusMe;
	
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

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public int getFriendsNum() {
		return friendsNum;
	}

	public void setFriendsNum(int friendsNum) {
		this.friendsNum = friendsNum;
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

	public int getMeNum() {
		return meNum;
	}

	public void setMeNum(int meNum) {
		this.meNum = meNum;
	}

}
