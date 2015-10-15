package com.hyk.utils;

import android.content.Context;

/**
 * @ClassName: DisplayUtil
 * @Description: TODO(dip��pxת��������)
 * @author linhs
 * @date 2013-12-6 ����2:24:05
 * 
 */
public class DisplayUtil {

	/**
	 * dipת��px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * pxת��dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
