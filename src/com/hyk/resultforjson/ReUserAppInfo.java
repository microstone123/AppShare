package com.hyk.resultforjson;

import java.util.List;

import com.hyk.user.UserAppInfo;

public class ReUserAppInfo {

	private int total;
	
	private int currentPage;
	
	private List<UserAppInfo> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public List<UserAppInfo> getList() {
		return list;
	}

	public void setList(List<UserAppInfo> list) {
		this.list = list;
	}
	
	
}
