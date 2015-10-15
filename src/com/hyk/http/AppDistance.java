package com.hyk.http;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class AppDistance {

	public static String APP_LIST_JULI = "距离";
	public static String APP_LIST_JUMI = "米拥有";
	public static String APP_LIST_GEYYONG = "个应用";
//	public static String APP_BIEMING_LIST = "别名:";
	public static String APP_TYPE_LIST = "类型:";
	public static String APP_STYPE_LIST = "大小:";
	public static String APP_LOADDOWN_NUM = "下载数次:";
	public static String APP_INTRODUCTION = "简介:";
	public static String APP_BTN_OPEN ="展开";
	public static String APP_BTN_INCOME ="收起";
	public static String APP_BTN_DETAIL ="详情";
	public static String APP_VERSION="版本:";
	public static String APP_SIZE="大小:";
	public static String APP_NAME="名称:";
	public static String APP_MORE="更多";
	public static String APP_COLLAPSE="收起";
	
	/**
	 * K 换算成 M 保留两位小数
	 * @param size
	 * @return
	 */
	public static String ktoM(int size){
		float c1 = (float) size / 1024 / 1024;
		BigDecimal d1 = new BigDecimal(c1).setScale(2, RoundingMode.CEILING);
		float d2 = d1.floatValue();
		return d2+"M";
	}
}
