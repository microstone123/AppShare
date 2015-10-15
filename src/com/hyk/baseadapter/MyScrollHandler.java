package com.hyk.baseadapter;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.hyk.view.DyPullListView;
import com.hyk.view.NestListView;
import com.hyk.view.PullListView;

@SuppressLint("HandlerLeak")
public class MyScrollHandler extends Handler {// /add by hzm

	private PullListView mListView;
	private NestListView listView;
	private DyPullListView nestListView;
	public MyScrollHandler(PullListView mListView,DyPullListView nestListView,NestListView listView) {
		this.mListView = mListView;
		this.nestListView = nestListView;
		this.listView = listView;
	}

	// 子类必须重写此方�?接受数据
	@Override
	public void handleMessage(Message msg) {

		super.handleMessage(msg);
		// 此处可以更新UI
		Bundle b = msg.getData();
		final int item = b.getInt("item");

		if(mListView !=null){
			mListView.startScrollListview(getHeightFromTop(mListView, item));	
		}
		if(nestListView !=null){
			nestListView.startScrollListview(getHeightFromTop(nestListView, item));	
		}
		if(listView !=null){
			listView.startScrollListview(getHeightFromTop(listView, item));	
		}
		
	}

	public int getHeightFromTop(ListView listView, int currentItem) {

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null || listAdapter.getCount() == 0) {
			// pre-condition
			return 0;
		}
		int totalHeight = 0;
		Rect r = new Rect();
		int firstVisablePose = listView.getFirstVisiblePosition();
		for (int i = 0; i <= currentItem - firstVisablePose; i++) {
			Boolean flag = listView.getChildAt(i).getGlobalVisibleRect(r); // 必须判断可见与否
			if (flag == true)
				totalHeight += r.height();
		}
		totalHeight = totalHeight + (listView.getDividerHeight() * (currentItem - firstVisablePose));
		return totalHeight;
	}

}
