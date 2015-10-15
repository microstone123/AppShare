package com.hyk.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hyk.activity.GuideActivity;
import com.hyk.activity.R;
import com.hyk.activity.TabMenuActivity;
import com.hyk.activity.WelcomeActivity;
import com.hyk.utils.ShortCutUtils;

public class CheckVersionTask implements Runnable {
	public static final int PARTTIME = 1000*5; // 停留时间
	private Context context;
	
	public CheckVersionTask(Context context) {
		super();
		this.context = context;
	}

	public void run() {
		try {
			Thread.sleep(PARTTIME);
			@SuppressWarnings("static-access")
			SharedPreferences sp = context.getSharedPreferences("CreateShortcut", context.MODE_PRIVATE);
			boolean firstuse = sp.getBoolean("firstUse", true);
			if (firstuse) {
				ShortCutUtils.addShortCut(context, context.getString(R.string.app_name), R.drawable.ic_launcher,
						WelcomeActivity.class);
				context.startActivity(new Intent(context, GuideActivity.class));
				((Activity) context).overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
			} else {
				loadMainUI();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			loadMainUI();
		}
	}

	/**
	 * 登陆主界面
	 */
	public void loadMainUI() {
		Intent intent = new Intent();
		intent.setClass(context, TabMenuActivity.class);
		intent.putExtra("isPic", false);
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(R.anim.push_rigt_out, R.anim.push_right_in);
	}
}
