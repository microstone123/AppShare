package com.hyk.myapp;

import android.graphics.drawable.Drawable;

public class Programe {
	// 图标
	private Drawable icon;
	// 程序名
	private String name;
	// 包名
	private String packName;
	// 大小
	private String size;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackName() {
		return packName;
	}

	public void setPackName(String packName) {
		this.packName = packName;
	}

}
