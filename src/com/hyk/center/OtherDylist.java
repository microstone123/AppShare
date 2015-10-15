package com.hyk.center;

import java.util.List;

public class OtherDylist {

	/**
	 * 时间
	 */
	private String makeTime;

	/**
	 * 动态列表
	 */
	private List<DynamicInfo> dayNewsList;

	public String getMakeTime() {
		return makeTime;
	}

	public void setMakeTime(String makeTime) {
		this.makeTime = makeTime;
	}

	public List<DynamicInfo> getDayNewsList() {
		return dayNewsList;
	}

	public void setDayNewsList(List<DynamicInfo> dayNewsList) {
		this.dayNewsList = dayNewsList;
	}

}
