package com.hyk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/***
 * �Զ�������ListView
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
		// ����ģʽ����ÿ��child�ĸ߶ȺͿ��
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
