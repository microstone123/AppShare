package com.hyk.center;

import java.util.List;

public class DyResult {
	
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
	private List<OtherDylist> otherlist;
	
	/**
	 * 今天动态
	 */
	private List<DynamicInfo> todayList;
	
	/**
	 * 昨天动态
	 */
	private List<DynamicInfo> yesterdayList;

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

	public List<OtherDylist> getOtherlist() {
		return otherlist;
	}

	public void setOtherlist(List<OtherDylist> otherlist) {
		this.otherlist = otherlist;
	}

	public List<DynamicInfo> getTodayList() {
		return todayList;
	}

	public void setTodayList(List<DynamicInfo> todayList) {
		this.todayList = todayList;
	}

	public List<DynamicInfo> getYesterdayList() {
		return yesterdayList;
	}

	public void setYesterdayList(List<DynamicInfo> yesterdayList) {
		this.yesterdayList = yesterdayList;
	}
	
	
}
