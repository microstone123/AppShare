package com.hyk.utils;

import java.util.Stack;

import android.app.Activity;
import android.content.Context;

/**
 * @ClassName: AppManager
 * @Description: TODO(应用程序Activity管理类)
 * @author linhaishi
 * @date 2013-8-21 下午4:42:42
 * 
 */
public class AppManager {
	private static Stack<Activity> mActivityStack;
	private static AppManager instance;
	
	private static Activity activity;

	private AppManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppManager getAppManager() {
		if (null == instance) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * 添加Activity到堆栈
	 */
	public void addActivity(Activity activity) {
		if (null == mActivityStack) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity() {
		Activity activity = mActivityStack.lastElement();
		return activity;
	}

	/**
	 * 获取Activity
	 */
	public Activity getActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return null;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = mActivityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			mActivityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : mActivityStack) {
			if (activity.getClass().equals(cls)) {
				mActivityStack.remove(activity);
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = mActivityStack.size(); i < size; i++) {
			if (null != mActivityStack.get(i)) {
				mActivityStack.get(i).finish();
			}
		}
		mActivityStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllActivity();
			// System.exit(0);
		} catch (Exception e) {
		}
	}

	public static Activity getActivity() {
		return activity;
	}

	public static void setActivity(Activity activity) {
		AppManager.activity = activity;
	}
	
}
