package com.hyk.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ListView;

/***
 * �Զ�������ListView
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
		// ����ģʽ����ÿ��child�ĸ߶ȺͿ��
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
