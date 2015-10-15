package com.hyk.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

/***
 * 自定义拖拉ListView
 */
public class NestedListView extends ListView {
	
	public NestedListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                   
                    return true ;
            default:
                    break;
            }
            return super.onTouchEvent(event);
    }

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 根据模式计算每个child的高度和宽度
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
