package com.hyk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hyk.activity.R;
import com.hyk.baseadapter.GallyImageAdapter;
import com.hyk.view.DetailsImgGallery;

/**
 * <p>
 * Title: CustomDialog
 * </p>
 * <p>
 * Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件）
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * 
 * @author archie
 * @version 1.0
 */
public class CustomDialog extends Dialog {
	private Context context;
	private String[] imgUrlList;
	private int id;

	public CustomDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public CustomDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CustomDialog(Context context, int theme, int id, String[] imgUrlList) {
		super(context, theme);
		this.context = context;
		this.imgUrlList = imgUrlList;
		this.id = id;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.customdialog);
		DetailsImgGallery galllery = (DetailsImgGallery) findViewById(R.id.mygallery);
		GallyImageAdapter imgAdapter = new GallyImageAdapter(context, imgUrlList);
		galllery.setAdapter(imgAdapter); // 设置图片ImageAdapter
		galllery.setSelection(id); // 设置当前显示图片
		Animation an = AnimationUtils.loadAnimation(context, R.anim.bigtosmallscale); // Gallery动画
		galllery.setAnimation(an);
	}

}