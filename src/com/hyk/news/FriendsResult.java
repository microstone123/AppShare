package com.hyk.news;

import java.util.List;

import com.hyk.center.DynamicInfo;

public class FriendsResult {
	
	/**
	 * 总数
	 */
	private int total;
	
	/**
	 * 当前页
	 */
	private int currentPage;
	
	/**
	 * 当前页个数
	 */
	private int pageSize;
	
	/**
	 * 其他天数动态
	 */
	private List<DynamicInfo> newslist;

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
