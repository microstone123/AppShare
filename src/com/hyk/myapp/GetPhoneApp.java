package com.hyk.myapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class GetPhoneApp {

	private static final int THE_RECENT_APP = 0;
	private static final int THE_MY_APP = 1;
	private static final int THE_SYS_APP = 2;
	private static final int THE_MY_RUN = 3;

	// 列出应用程序
	public synchronized static List<LocationApp> loadApps(Context context) {
		MyappDatabaseUtil myappDatabaseUtil = new MyappDatabaseUtil(context);
		List<LocationApp> appInfoList = new ArrayList<LocationApp>();
		List<LocationApp> newList = new ArrayList<LocationApp>();
		try {
			// 得到PackageManager对象
			PackageManager pm = context.getPackageManager();
			// 得到系统安装的所有程序包的PackageInfo对象
			List<PackageInfo> packages = pm.getInstalledPackages(0);
			for (PackageInfo info : packages) {

				LocationApp appInfo = new LocationApp();
				appInfo.setPackName(info.applicationInfo.processName);
				long length = new File(info.applicationInfo.publicSourceDir).length();
				double size = length / 1024;
				appInfo.setAppSize(String.valueOf(size));
				appInfo.setAppVersion(info.versionName);
				appInfo.setName(info.applicationInfo.loadLabel(pm).toString());
				appInfo.setAppDrawable(info.applicationInfo.loadIcon(pm));
				// 列出我的应用
				if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
//					myappDatabaseUtil.insert(info.applicationInfo.loadLabel(pm).toString(),
//							info.applicationInfo.processName, info.applicationInfo.loadIcon(pm), null,
//							String.valueOf(size), THE_MY_APP);
					appInfoList.add(appInfo);
				}
				// 列出系统应用，总是感觉这里设计的有问题，希望高手指点
				if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
					myappDatabaseUtil.insert(info.applicationInfo.loadLabel(pm).toString(),
							info.applicationInfo.processName, info.applicationInfo.loadIcon(pm), null,
							String.valueOf(size), THE_SYS_APP);
				}
			}
			
			
			List<LocationApp> oldList = myappDatabaseUtil.queryForTime(THE_MY_APP);
			if (oldList != null) {
				if (oldList.size() > 0) {
					for (int i = 0; i < oldList.size(); i++) {
						for (int j = 0; j < appInfoList.size(); j++) {
							if ((appInfoList.get(j).getName()).equals(oldList.get(i).getName())) {
								appInfoList.remove(j);
								break;
							}

						}
					}

					if (appInfoList.size() != 0) {
						for (int t = 0; t < appInfoList.size(); t++) {
							newList.add(appInfoList.get(t));
							myappDatabaseUtil.insert(appInfoList.get(t).getName(), appInfoList.get(t).getPackName(), appInfoList.get(t)
									.getAppDrawable(), null, appInfoList.get(t).getAppSize(), THE_MY_APP);
						}
						for (int t = 0; t < oldList.size(); t++) {
							newList.add(oldList.get(t));
						}
					} else {
						for (int t = 0; t < oldList.size(); t++) {
							newList.add(oldList.get(t));
						}
					}

				} else {
					for (int t = 0; t < appInfoList.size(); t++) {
						myappDatabaseUtil.insert(appInfoList.get(t).getName(), appInfoList.get(t).getPackName(), appInfoList.get(t)
								.getAppDrawable(), null, appInfoList.get(t).getAppSize(), THE_MY_APP);
						newList.add(appInfoList.get(t));
					}
				}
			} else {
				for (int t = 0; t < appInfoList.size(); t++) {
					myappDatabaseUtil.insert(appInfoList.get(t).getName(), appInfoList.get(t).getPackName(), appInfoList.get(t)
							.getAppDrawable(), null, appInfoList.get(t).getAppSize(), THE_RECENT_APP);
					newList.add(appInfoList.get(t));
				}
			}
			
			
			
			
			
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return newList;
	}

	public synchronized static void getLocadApp(Context context) {
		loadApps(context);
		MyappDatabaseUtil myappDatabaseUtil = new MyappDatabaseUtil(context);
		List<LocationApp> prList = getRunningProcess(context);
		try {
			for (int i = 0; i < prList.size(); i++) {

				long length = new File(prList.get(i).getAppSize()).length();
				double size = length / 1024;

				myappDatabaseUtil.insert(prList.get(i).getName(), prList.get(i).getPackName(), prList.get(i)
						.getAppDrawable(), null, String.valueOf(size), THE_RECENT_APP);

				for (int y = 0; y < StaticAppInfo.getStaticAppInfo().getMyAppList().size(); y++) {
					if (prList.get(i).getPackName()
							.equals(StaticAppInfo.getStaticAppInfo().getMyAppList().get(y).getPackName())) {

						myappDatabaseUtil.insert(prList.get(i).getName(), prList.get(i).getPackName(), prList.get(i)
								.getAppDrawable(), null, String.valueOf(size), THE_MY_RUN);
					}
				}
			}
			StaticAppInfo.getStaticAppInfo().setRecentlyAppList(myappDatabaseUtil.queryForTime(THE_RECENT_APP));
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 正在运行的
	public static List<LocationApp> getRunningInToDataBase(Context context) {
		List<LocationApp> list = new ArrayList<LocationApp>();
		try {
			MyappDatabaseUtil myappDatabaseUtil = new MyappDatabaseUtil(context);
			PackagesInfo pi = new PackagesInfo(context);
			ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
			// 获取正在运行的应用
			List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
			// 获取包管理器，在这里主要通过包名获取程序的图标和程序名
			PackageManager pm = context.getPackageManager();
			for (RunningAppProcessInfo ra : run) {
				// 这里主要是过滤系统的应用和电话应用，当然你也可以把它注释掉。
				if (ra.processName.equals("system") || ra.processName.equals("com.android.phone")) {
					continue;
				}
				if (pi.getInfo(ra.processName) == null) {
					continue;
				}
				LocationApp pr = new LocationApp();
				pr.setAppDrawable(pi.getInfo(ra.processName).loadIcon(pm));
				pr.setPackName(ra.processName);
				pr.setName(pi.getInfo(ra.processName).loadLabel(pm).toString());
				long length = new File(pi.getInfo(ra.processName).sourceDir).length();
				double size = length / 1024;
				pr.setAppSize(String.valueOf(size));
				list.add(pr);
				myappDatabaseUtil.insert(pi.getInfo(ra.processName).loadLabel(pm).toString(), ra.processName, pi
						.getInfo(ra.processName).loadIcon(pm), null, String.valueOf(size), THE_RECENT_APP);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	// 正在运行的
	public static List<LocationApp> getRunningProcess(Context context) {
		List<LocationApp> list = new ArrayList<LocationApp>();
		List<LocationApp> newList = new ArrayList<LocationApp>();
		MyappDatabaseUtil myappDatabaseUtil = new MyappDatabaseUtil(context);
		try {
			PackagesInfo pi = new PackagesInfo(context);
			ActivityManager am = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
			// 获取正在运行的应用
			List<RunningAppProcessInfo> run = am.getRunningAppProcesses();
			// 获取包管理器，在这里主要通过包名获取程序的图标和程序名
			PackageManager pm = context.getPackageManager();
			for (RunningAppProcessInfo ra : run) {

				if (!ra.processName.startsWith("android.")) {
					// 这里主要是过滤系统的应用和电话应用，当然你也可以把它注释掉。
					if (ra.processName.equals("system") || ra.processName.equals("com.android.phone")) {
						continue;
					}
					if (pi.getInfo(ra.processName) == null) {
						continue;
					}
					LocationApp pr = new LocationApp();
//					Log.e("ra.processName", ra.processName + " " + pi.getInfo(ra.processName).loadLabel(pm).toString());
					pr.setAppDrawable(pi.getInfo(ra.processName).loadIcon(pm));
					pr.setPackName(ra.processName);
					pr.setName(pi.getInfo(ra.processName).loadLabel(pm).toString());
					long length = new File(pi.getInfo(ra.processName).sourceDir).length();
					double size = length / 1024;
					pr.setAppSize(String.valueOf(size));
					list.add(pr);
				}
			}

			List<LocationApp> oldList = myappDatabaseUtil.queryForTime(THE_RECENT_APP);
			if (oldList != null) {
				if (oldList.size() > 0) {
					for (int i = 0; i < oldList.size(); i++) {
						for (int j = 0; j < list.size(); j++) {
							if ((list.get(j).getName()).equals(oldList.get(i).getName())) {
								list.remove(j);
								break;
							}

						}
					}

					if (list.size() != 0) {
						for (int t = 0; t < list.size(); t++) {
							newList.add(list.get(t));
							myappDatabaseUtil.insert(list.get(t).getName(), list.get(t).getPackName(), list.get(t)
									.getAppDrawable(), null, list.get(t).getAppSize(), THE_RECENT_APP);
						}
						for (int t = 0; t < oldList.size(); t++) {
							newList.add(oldList.get(t));
						}
					} else {
						for (int t = 0; t < oldList.size(); t++) {
							newList.add(oldList.get(t));
						}
					}

				} else {
					for (int t = 0; t < list.size(); t++) {
						myappDatabaseUtil.insert(list.get(t).getName(), list.get(t).getPackName(), list.get(t)
								.getAppDrawable(), null, list.get(t).getAppSize(), THE_RECENT_APP);
						newList.add(list.get(t));
					}
				}
			} else {
				for (int t = 0; t < list.size(); t++) {
					myappDatabaseUtil.insert(list.get(t).getName(), list.get(t).getPackName(), list.get(t)
							.getAppDrawable(), null, list.get(t).getAppSize(), THE_RECENT_APP);
					newList.add(list.get(t));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return newList;
	}
	
	
	
	// 列出应用程序
	public synchronized static List<LocationApp> myLoadApps(Context context) {
		List<LocationApp> appInfoList = new ArrayList<LocationApp>();
		try {
			// 得到PackageManager对象
			PackageManager pm = context.getPackageManager();
			// 得到系统安装的所有程序包的PackageInfo对象
			List<PackageInfo> packages = pm.getInstalledPackages(0);
			for (PackageInfo info : packages) {

				for (int i = 0; i < appInfoList.size(); i++) {
					if(info.applicationInfo.processName.endsWith(appInfoList.get(i).getPackName())){
						appInfoList.remove(i);
						break;
					}
				}
				
				LocationApp appInfo = new LocationApp();
				appInfo.setPackName(info.applicationInfo.processName);
				long length = new File(info.applicationInfo.publicSourceDir).length();
				double size = length / 1024;
				appInfo.setAppSize(String.valueOf(size));
				appInfo.setAppVersion(info.versionName);
				appInfo.setName(info.applicationInfo.loadLabel(pm).toString());
				appInfo.setAppDrawable(info.applicationInfo.loadIcon(pm));
				// 列出我的应用
				if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
					appInfoList.add(appInfo);
				}
			}
			
		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return appInfoList;
	}
}
