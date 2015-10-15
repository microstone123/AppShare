package com.hyk.user;

import java.util.List;

public class NearestUser {

	/**
	 * 搜索到的用户集合,具体参见userList对象
	 */
	private List<UserList> list;
	/**
	 * 总数
	 */
	private int total;

	/**
	 * 当前页数，从1开始
	 */
	private int currentPage;

	/**
	 * 每页多少条
	 */
	private int pageSize;

	public List<UserList> getList() {
		return list;
	}

	public void setList(List<UserList> list) {
		this.list = list;
	}

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

}
