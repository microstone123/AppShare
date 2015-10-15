package com.hyk.myapp;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

public class UpdateList {
	
	private int status;
	
	private List<Integer> list = new ArrayList<Integer>();
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

}
