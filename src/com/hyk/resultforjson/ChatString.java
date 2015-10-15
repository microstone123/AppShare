package com.hyk.resultforjson;

import java.util.List;

import com.hyk.chat.ChatHistryString;


public class ChatString {

	private int currentPage;
	private int total;
	private String myRegId;
	private String myUserName;
	private String myHeadPic;
	private String hisRegId;
	private String hisUserName;
	private String hisHeadPic;
	private List<ChatHistryString> list;
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getMyRegId() {
		return myRegId;
	}
	public void setMyRegId(String myRegId) {
		this.myRegId = myRegId;
	}
	public String getMyUserName() {
		return myUserName;
	}
	public void setMyUserName(String myUserName) {
		this.myUserName = myUserName;
	}
	public String getMyHeadPic() {
		return myHeadPic;
	}
	public void setMyHeadPic(String myHeadPic) {
		this.myHeadPic = myHeadPic;
	}
	public String getHisRegId() {
		return hisRegId;
	}
	public void setHisRegId(String hisRegId) {
		this.hisRegId = hisRegId;
	}
	public String getHisUserName() {
		return hisUserName;
	}
	public void setHisUserName(String hisUserName) {
		this.hisUserName = hisUserName;
	}
	public String getHisHeadPic() {
		return hisHeadPic;
	}
	public void setHisHeadPic(String hisHeadPic) {
		this.hisHeadPic = hisHeadPic;
	}
	public List<ChatHistryString> getList() {
		return list;
	}
	public void setList(List<ChatHistryString> list) {
		this.list = list;
	}
}
