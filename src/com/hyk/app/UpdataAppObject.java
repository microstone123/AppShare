package com.hyk.app;

import java.util.List;

public class UpdataAppObject {

	/**
	 * �ܸ���
	 */
	private int total;

	/**
	 * ���´�С
	 */
	private float size;

	/**
	 * ��ǰҳ
	 */
	private int currentPage;

	/**
	 * ҳ��С
	 */
	private int pageSize;

	/**
	 * �����б�
	 */
	private List<UpdataApp> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public float getSize() {
		return size;
	}

	public void setSize(float size) {
		this.size = size;
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

	public List<UpdataApp> getList() {
		return list;
	}

	public void setList(List<UpdataApp> list) {
		this.list = list;
	}

}
