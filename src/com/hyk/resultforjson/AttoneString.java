package com.hyk.resultforjson;

import java.util.List;

import com.hyk.news.AttentionInfo;

public class AttoneString {

	private int currentPage;
	private int total;
	private List<AttentionInfo> apps;
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
	public List<AttentionInfo> getApps() {
		return apps;
	}
	public void setApps(List<AttentionInfo> apps) {
		this.apps = apps;
	}

}
