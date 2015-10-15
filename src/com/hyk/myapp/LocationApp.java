package com.hyk.myapp;

import android.graphics.drawable.Drawable;

/**
 * @ClassName: LocationApp
 * @Description: TODO(常用应用)
 * @author linhs
 * @date 2013-12-24 下午2:07:45
 */
public class LocationApp {
	
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 包名
	 */
	private String packName;
	/**
	 * applogo
	 */
	private byte[] app_img;
	
	/**
	 * Drawable
	 */
	private Drawable appDrawable;
	/**
	 * 打开时间
	 */
	private String openTime;
	/**
	 * 类型
	 */
	private int statics;
	
	private String appSize;
	
	private String appVersion;
	
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
	public LocationApp(String name) {
		super();
		this.name = name;
	}
	public LocationApp() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getPackName() {
		return packName;
	}
	public void setPackName(String packName) {
		this.packName = packName;
	}
	public byte[] getApp_img() {
		return app_img;
	}
	public void setApp_img(byte[] app_img) {
		this.app_img = app_img;
	}
	public int getStatics() {
		return statics;
	}
	public void setStatics(int statics) {
		this.statics = statics;
	}
	public Drawable getAppDrawable() {
		return appDrawable;
	}
	public void setAppDrawable(Drawable appDrawable) {
		this.appDrawable = appDrawable;
	}

}
