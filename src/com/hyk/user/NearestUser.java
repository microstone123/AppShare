package com.hyk.user;

import java.util.List;

public class NearestUser {

	/**
	 * ���������û�����,����μ�userList����
	 */
	private List<UserList> list;
	/**
	 * ����
	 */
	private int total;

	/**
	 * ��ǰҳ������1��ʼ
	 */
	private int currentPage;

	/**
	 * ÿҳ������
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
