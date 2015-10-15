package com.hyk.resultforjson;

import java.util.List;

import com.hyk.center.DynamicInfo;

public class FriendDyString {

	private int currentPage;
	private int total;
	private int pageSize;
	private List<DynamicInfo> newslist;
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
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<DynamicInfo> getNewslist() {
		return newslist;
	}
	public void setNewslist(List<DynamicInfo> newslist) {
		this.newslist = newslist;
	}
	
	
	
}
