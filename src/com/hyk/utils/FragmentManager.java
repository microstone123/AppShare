package com.hyk.utils;

import java.util.Stack;

import android.support.v4.app.Fragment;

/**
 * @ClassName: FragmentManager
 * @Description: TODO(应用程序Fragment管理类)
 * @author linhaishi
 * @date 2013-8-21 下午4:42:42
 * 
 */
public class FragmentManager {
	private static Stack<Fragment> mFragmentStack;
	private static FragmentManager instance;

	private FragmentManager() {
	}

	/**
	 * 单一实例
	 */
	public static FragmentManager getAppManager() {
		if (null == instance) {
			instance = new FragmentManager();
		}
		return instance;
	}

	/**
	 * 添加Fragment到堆栈
	 */
	public void addFragment(Fragment fragment) {
		if (null == mFragmentStack) {
			mFragmentStack = new Stack<Fragment>();
		}
		mFragmentStack.add(fragment);
	}

	/**
	 * 获取当前Fragment（堆栈中最后一个压入的）
	 */
	public Fragment currentFragment() {
		Fragment fragment = mFragmentStack.lastElement();
		return fragment;
	}

	/**
	 * 获取指定Fragment
	 */
	public Fragment getFragment(Class<?> cls) {
		for (Fragment fragment : mFragmentStack) {
			if (fragment.getClass().equals(cls)) {
				return fragment;
			}
		}
		return null;
	}

}
