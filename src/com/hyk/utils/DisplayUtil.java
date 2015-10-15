package com.hyk.utils;

import android.content.Context;

/**
 * @ClassName: DisplayUtil
 * @Description: TODO(dip与px转换工具类)
 * @author linhs
 * @date 2013-12-6 下午2:24:05
 * 
 */
public class DisplayUtil {

	/**
	 * dip转换px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转换dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
