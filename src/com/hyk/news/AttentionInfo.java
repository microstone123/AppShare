package com.hyk.news;

import java.util.List;

public class AttentionInfo {

	private int regId;
	private String headPic;
	private String signName;
	private String userName;
	private String imei;
	private List<AttentionApp> list;

	public int getRegId() {
		return regId;
	}

	public void setRegId(int regId) {
		this.regId = regId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<AttentionApp> getList() {
		return list;
	}

	public void setList(List<AttentionApp> list) {
		this.list = list;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
