package com.hyk.utils;

import java.util.List;
import java.util.Random;

import com.hyk.user.UserList;

public class RoundPoint {

	/**
	 * app������
	 */
	public static long Big_Distance = 1;
	/**
	 * ����ת���ǶȲ���
	 */
	public static double PARAMETER = Math.PI / 180;
	/**
	 * X Y �����ֵ
	 */
	public static int[] BIG_XY_RADIUS = { 445, 448 };
	/**
	 * ��������
	 */
	public static int[] CENTER_RADIUS = new int[2];
	/**
	 * Y ������������ֵ
	 */
	public static int BIG_Y_RADIUS;

	/**
	 * X ������������ֵ
	 */
	public static int RADIUS_Screen_ = 193;

	/**
	 * ��ȡX ��ֱ��ʱ���
	 */
	public static int getXForResolution(int ImgViewWidth) {
		return ImgViewWidth / 445;
	}

	/**
	 * ��ȡY ��ֱ��ʱ���
	 */
	public static int getYForResolution(int ImgViewHeight) {
		return ImgViewHeight / 448;
	}

	/**
	 * ��ȡ�û�����Ļ�ϵ������
	 * 
	 * @param appList
	 * @return
	 */
	public static long[] getPoinXY(UserList appList) {
		Random random = new Random();
		int fdfd = 100+random.nextInt(385500); // �����ȡһ���Ƕ�
		long[] XY = new long[2]; // ��������
		long radius = fdfd * BIG_Y_RADIUS / Big_Distance; // ���״��ϵİ뾶
		if (radius > BIG_Y_RADIUS) {
			radius = BIG_Y_RADIUS;
		}
		int angle = random.nextInt(360); // �����ȡһ���Ƕ�
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
	 * ��ȡapp�б��е�������
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
