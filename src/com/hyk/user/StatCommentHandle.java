package com.hyk.user;

import android.os.Handler;
import android.support.v4.app.Fragment;

public class StatCommentHandle {
	private static StatCommentHandle statCommentHandle = new StatCommentHandle();
	private Handler handler;
	private int checkItem =-1;
	private int item =-1;
	private Fragment fragment;

	public static StatCommentHandle getStatCommentHandle() {
		return statCommentHandle;
	}

	public static void setStatCommentHandle(StatCommentHandle statCommentHandle) {
		StatCommentHandle.statCommentHandle = statCommentHandle;
	}

	public int getCheckItem() {
		return checkItem;
	}

	public Fragment getFragment() {
		return fragment;
	}

	public void setFragment(Fragment fragment) {
		this.fragment = fragment;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public void setCheckItem(int checkItem) {
		this.checkItem = checkItem;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}
