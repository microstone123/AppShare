package com.hyk.center;

import java.util.List;

public class DyResult {
	
	/**
	 * ����
	 */
	private int total;
	
	/**
	 * ��ǰҳ
	 */
	private int currentPage;
	
	/**
	 * ��ǰҳ����
	 */
	private int pageSize;
	
	/**
	 * ����������̬
	 */
	private List<OtherDylist> otherlist;
	
	/**
	 * ���춯̬
	 */
	private List<DynamicInfo> todayList;
	
	/**
	 * ���춯̬
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
