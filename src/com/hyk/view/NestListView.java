package com.hyk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/***
 * 自定义拖拉ListView
 * 
 * @author zhangjia
 * 
 */
public class NestListView extends ListView {

	public NestListView(Context context) {
		super(context, null);
	}

	public NestListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 根据模式计算每个child的高度和宽度
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	public void startScrollListview(int height) { //
		smoothScrollBy(height, 100);// 滑动距离和时间 负数将向下滑动
	}

}
