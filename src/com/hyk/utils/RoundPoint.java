package com.hyk.utils;

import java.util.List;
import java.util.Random;

import com.hyk.user.UserList;

public class RoundPoint {

	/**
	 * app最大距离
	 */
	public static long Big_Distance = 1;
	/**
	 * 弧度转化角度参数
	 */
	public static double PARAMETER = Math.PI / 180;
	/**
	 * X Y 轴最大值
	 */
	public static int[] BIG_XY_RADIUS = { 445, 448 };
	/**
	 * 中心坐标
	 */
	public static int[] CENTER_RADIUS = new int[2];
	/**
	 * Y 轴距离中心最大值
	 */
	public static int BIG_Y_RADIUS;

	/**
	 * X 轴距离中心最大值
	 */
	public static int RADIUS_Screen_ = 193;

	/**
	 * 获取X 轴分辨率比例
	 */
	public static int getXForResolution(int ImgViewWidth) {
		return ImgViewWidth / 445;
	}

	/**
	 * 获取Y 轴分辨率比例
	 */
	public static int getYForResolution(int ImgViewHeight) {
		return ImgViewHeight / 448;
	}

	/**
	 * 获取用户在屏幕上的坐标点
	 * 
	 * @param appList
	 * @return
	 */
	public static long[] getPoinXY(UserList appList) {
		Random random = new Random();
		int fdfd = 100+random.nextInt(385500); // 随机获取一个角度
		long[] XY = new long[2]; // 定义坐标
		long radius = fdfd * BIG_Y_RADIUS / Big_Distance; // 在雷达上的半径
		if (radius > BIG_Y_RADIUS) {
			radius = BIG_Y_RADIUS;
		}
		int angle = random.nextInt(360); // 随机获取一个角度
		XY[0] = getXRadius(angle, radius);
		XY[1] = getYRadius(angle, radius);
		return XY;
	}

	public static long getXRadius(int angle, long radius) {
		long x = 0;
		int newAngle = (int) Math.floor(radius * Math.sin(PARAMETER * angle));
		if (newAngle < 0) {
			newAngle = -newAngle;
		}
		if (angle >= 90 && angle <= 270) {
			x = CENTER_RADIUS[0] + newAngle;
		} else {
			x = CENTER_RADIUS[0] - newAngle;
		}
		return x;
	}

	public static long getYRadius(int angle, long radius) {
		long y = 0;
		int newAngle = (int) Math.floor(radius * Math.cos(PARAMETER * angle));
		if (newAngle < 0) {
			newAngle = -newAngle;
		}
		if (angle >= 180 && angle <= 360) {
			y = CENTER_RADIUS[1] - newAngle;
		} else {
			y = CENTER_RADIUS[1] + newAngle;
		}
		return y;
	}

	/**
	 * 获取app列表中的最大距离
	 */
	public static void getBigResol(List<UserList> appList, int w, int h) {
		BIG_Y_RADIUS = w / 2 - 50;
		CENTER_RADIUS[0] = w / 2 - 20 * w / 200;
		CENTER_RADIUS[1] = w / 2 - 20 * w / 200;
		Big_Distance = 385600;
//		if (appList.size() > 0) {
//			for (int i = 0; i < appList.size(); i++) {
//				if (appList.get(i).getDistance() > Big_Distance) {
//					Big_Distance = appList.get(i).getDistance();
//				}
//			}
//		}
	}
}
