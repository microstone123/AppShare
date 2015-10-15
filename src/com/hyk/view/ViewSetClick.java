package com.hyk.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hyk.activity.R;

/**
 * @ClassName: ViewSetClick
 * @Description: TODO(View点击特效)
 * @author linhs
 * @date 2014-2-20 下午6:36:54
 */
public class ViewSetClick {
	
	/**
	 * 点击变暗效果
	 */
	public static void getAlpha(View v,Context context){
		Animation shakeAnim = AnimationUtils.loadAnimation(context, R.anim.alpha);
		v.startAnimation(shakeAnim);
	}
	
	/**
	 * 点击变暗效果
	 */
	public static void getLongAlpha(View v,Context context){
		Animation shakeAnim = AnimationUtils.loadAnimation(context, R.anim.long_alpha);
		v.startAnimation(shakeAnim);
	}
	
	/**
	 * 点击变暗效果
	 */
	public static void getShortAlpha(View v,Context context){
		Animation shakeAnim = AnimationUtils.loadAnimation(context, R.anim.short_alpha);
		v.startAnimation(shakeAnim);
	}
}
