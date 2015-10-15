package com.hyk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;
import android.widget.Toast;

import com.hyk.baseadapter.GallyImageAdapter;

@SuppressWarnings("deprecation")
public class DetailsImgGallery extends Gallery {
	boolean isFirst = false;
	boolean isLast = false;

	public DetailsImgGallery(Context context) {
		super(context);
	}

	public DetailsImgGallery(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
	}

	/** �Ƿ����󻬶���true - ���󻬶��� false - ���һ����� */
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		GallyImageAdapter ia = (GallyImageAdapter) this.getAdapter();
		int p = ia.getOwnposition(); // ��ȡ��ǰͼƬ��position
		int count = ia.getCount(); // ��ȡȫ��ͼƬ������count
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			if (p == 0 && isFirst) {
				Toast.makeText(this.getContext(), "���ǵ�һ��", Toast.LENGTH_SHORT).show();
			} else if (p == 0) {
				isFirst = true;
			} else {
				isLast = false;
			}

			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			if (p == count - 1 && isLast) {
				Toast.makeText(this.getContext(), "�ѵ����һ��", Toast.LENGTH_SHORT).show();
			} else if (p == count - 1) {
				isLast = true;
			} else {
				isFirst = false;
			}

			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;
	}
}