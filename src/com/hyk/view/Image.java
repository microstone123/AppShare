package com.hyk.view;

import java.io.Serializable;

/************************************************************
 *  内容摘要	：
 *
 *  作者	：adminstrator
 *  创建时间	：2013-7-23 下午05:19:47 
 *  当前版本号：v1.0
 *  历史记录	:
 *  	日期	: 2013-7-23 下午05:19:47 	修改人：adminstrator
 *  	描述	:
 ************************************************************/
public class Image implements Serializable {
	private String path;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
