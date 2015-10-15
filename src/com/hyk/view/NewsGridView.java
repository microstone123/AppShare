package com.hyk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/***
 * 自定义拖拉ListView
 * 
 * @author zhangjia
 * 
 */
public class NewsGridView extends GridView {

	public NewsGridView(Context context) {
		super(context, null);
	}

	public NewsGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 根据模式计算每个child的高度和宽度
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
