package com.hyk.download;

import android.os.Handler;

public class DownloadHandle {

	private int downloadId;

	private Handler handler;

	private String downlUrl;
	
	private String appName;
	
	private int statics;

	public int getDownloadId() {
		return downloadId;
	}

	public void setDownloadId(int downloadId) {
		this.downloadId = downloadId;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public String getDownlUrl() {
		return downlUrl;
	}

	public void setDownlUrl(String downlUrl) {
		this.downlUrl = downlUrl;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public DownloadHandle(int downloadId, Handler handler, String downlUrl, String appName,int statics) {
		super();
		this.downloadId = downloadId;
		this.handler = handler;
		this.downlUrl = downlUrl;
		this.appName = appName;
		this.statics = statics;
	}

	public DownloadHandle() {
		super();
	}

	public int getStatics() {
		return statics;
	}

	public void setStatics(int statics) {
		this.statics = statics;
	}

	

}
