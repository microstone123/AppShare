package com.hyk.resultforjson;

import java.util.List;

import com.hyk.news.NewsForInfo;

public class NewsInfoString {

	private int currentPage;
	private int total;
	private int totalPage;
	private List<NewsForInfo> list;
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
	public List<NewsForInfo> getList() {
		return list;
	}
	public void setList(List<NewsForInfo> list) {
		this.list = list;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
