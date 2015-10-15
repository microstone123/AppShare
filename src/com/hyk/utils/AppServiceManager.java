package com.hyk.utils;

import java.util.Stack;

import android.app.Service;
import android.content.Context;

/**
 * @ClassName: AppManager
 * @Description: TODO(应用程序Service管理类)
 * @author linhaishi
 * @date 2013-8-21 下午4:42:42
 * 
 */
public class AppServiceManager {
	private static Stack<Service> mServiceStack;
	private static AppServiceManager instance;

	private AppServiceManager() {
	}

	/**
	 * 单一实例
	 */
	public static AppServiceManager getAppManager() {
		if (null == instance) {
			instance = new AppServiceManager();
		}
		return instance;
	}

	/**
	 * 添加Service到堆栈
	 */
	public void addService(Service service) {
		if (null == mServiceStack) {
			mServiceStack = new Stack<Service>();
		}
		mServiceStack.add(service);
	}

	/**
	 * 获取当前Service（堆栈中最后一个压入的）
	 */
	public Service currentService() {
		Service service = mServiceStack.lastElement();
		return service;
	}

	/**
	 * 获取Service
	 */
	public Service getService(Class<?> cls) {
		if (mServiceStack == null)
			return null;
		for (Service service : mServiceStack) {
			if (service.getClass().equals(cls)) {
				return service;
			}
		}
		return null;
	}

	/**
	 * 结束当前Service（堆栈中最后一个压入的）
	 */
	public void finishService() {
		Service service = mServiceStack.lastElement();
		if (service != null) {
			service.stopSelf();
			service = null;
		}
	}

	/**
	 * 结束指定的Service
	 */
	public void finishService(Service service) {
		if (service != null) {
			mServiceStack.remove(service);
			service.stopSelf();
			service = null;
		}
	}

	/**
	 * 结束指定类名的Service
	 */
	public void finishService(Class<?> cls) {
		for (Service service : mServiceStack) {
			if (service.getClass().equals(cls)) {
				finishService(service);
			}
		}
	}

	/**
	 * 结束所有Service
	 */
	public void finishAllService() {
		for (int i = 0, size = mServiceStack.size(); i < size; i++) {
			if (null != mServiceStack.get(i)) {
				mServiceStack.get(i).stopSelf();
			}
		}
		mServiceStack.clear();
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit(Context context) {
		try {
			finishAllService();
			// System.exit(0);
		} catch (Exception e) {
		}
	}
}
