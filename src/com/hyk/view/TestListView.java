package com.hyk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.hyk.activity.R;

/***
 * �Զ�������ListView
 * 
 * @author zhangjia
 * 
 */
public class TestListView extends ListView {

	public TestListView(Context context) {
		super(context, null);
	}

	public TestListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View mHeadView = LayoutInflater.from(context).inflate(R.layout.test_dynamic, null);
		addHeaderView(mHeadView);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ����ģʽ����ÿ��child�ĸ߶ȺͿ��
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
