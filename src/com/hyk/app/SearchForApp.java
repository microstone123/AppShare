package com.hyk.app;

import java.util.List;

import com.hyk.user.UserAppInfo;

public class SearchForApp {

	/**
	 * ���������û�����,����μ�userList����
	 */
	private List<UserAppInfo> list;
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

	public List<UserAppInfo> getList() {
		return list;
	}

	public void setList(List<UserAppInfo> list) {
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
