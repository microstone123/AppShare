package com.hyk.xmpp.data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DateUtil {
	public final static String yyyy = "yyyy";
	public final static String yyyy_MM_dd = "yyyy-MM-dd";
	public final static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";
	public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
	public final static String yyyy_MM_dd_HH_mm_ss_SSS = "yyyy-MM-dd HH:mm:ss SSS";
	public final static String MM_dd_HH_mm_ss = "MM-dd  HH:mm:ss";

	public static String now(String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(Calendar.getInstance().getTime());
	}

	public static String now_yyyy() {
		return now(yyyy);
	}

	public static String now_yyyy_MM_dd() {
		return now(yyyy_MM_dd);
	}

	public static String now_yyyy_MM_dd_HH_mm_ss() {
		return now(yyyy_MM_dd_HH_mm_ss);
	}

	public static String now_yyyy_MM_dd_HH_mm_ss_SSS() {
		return now(yyyy_MM_dd_HH_mm_ss_SSS);
	}

	public static String now_MM_dd_HH_mm_ss() {
		return now(MM_dd_HH_mm_ss);
	}

	/**
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	/**
	 * ������ʱ����ת���ַ��� ��:" 2002-07-01 11:40:02"
	 * 
	 * @param inDate
	 *            ����ʱ�� " 2002-07-01 11:40:02"
	 * @return String ת��������ʱ���ַ���
	 */
	public static String dateToStr_yyyy_MM_dd_HH_mm_ss(Date date) {
		return dateToStr(date, yyyy_MM_dd_HH_mm_ss);
	}

	/**
	 * ������ʱ����ת���ַ��� ��:" 2002-07-01 11:40:02"
	 * 
	 * @param inDate
	 *            ����ʱ�� " 2002-07-01 11:40:02"
	 * @return String ת��������ʱ���ַ���
	 */
	public static String dateToStr_MM_dd_HH_mm_ss(Date date) {
		return dateToStr(date, MM_dd_HH_mm_ss);
	}

	/**
	 * ��������ת���ַ��� ��:"2002-07-01"
	 * 
	 * @param inDate
	 *            ���� "2002-07-01"
	 * @return String ת���������ַ���
	 */
	public static String dateToStr_yyyy_MM_dd(Date date) {
		return dateToStr(date, yyyy_MM_dd);
	}

	/**
	 * ���ַ�����(Ӣ�ĸ�ʽ)ת�������� ��: "Tue Dec 26 14:45:20 CST 2000"
	 * 
	 * @param DateFormatStr
	 *            �ַ��� "Tue Dec 26 14:45:20 CST 2000"
	 * @return Date ����
	 */
	public static Date strToDateEN(String shorDateStr) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss 'CST' yyyy", java.util.Locale.US);
			return sdf.parse(shorDateStr);
		} catch (Exception e) {
			return new Date();
		}
	}

	/**
	 * ���ַ�����(���ĸ�ʽ)ת�������� ��:"2002-07-01 22:09:55"
	 * 
	 * @param datestr
	 *            �ַ��� "2002-07-01 22:09:55"
	 * @return Date ����
	 */
	public static Date strToDateCN_yyyy_MM_dd_HH_mm_ss(String datestr) {
		Date date = null;
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = fmt.parse(datestr);
		} catch (Exception e) {
			return date;
		}
		return date;
	}

	public static Date strToDateMM_dd(String datestr) {
		Date date = null;
		try {
			SimpleDateFormat fmt = new SimpleDateFormat(MM_dd_HH_mm_ss);
			date = fmt.parse(datestr);
		} catch (Exception e) {
			return date;
		}
		return date;
	}

	/**
	 * 
	 * @param datestr
	 * @return
	 */
	public static Date strToDateCN_yyyy_MM_dd(String datestr) {
		Date date = null;
		try {
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
			date = fmt.parse(datestr);
		} catch (Exception e) {
			return date;
		}
		return date;
	}

	/**
	 * ת��util.date-->sql.date
	 * 
	 * @param inDate
	 * @return
	 */
	public static java.sql.Date UtilDateToSqlDate(Date inDate) {
		return new java.sql.Date(getDateTime(inDate));
	}

	private static long getDateTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		cal.set(year, month, day, 0, 0, 0);
		long result = cal.getTimeInMillis();
		result = result / 1000 * 1000;
		return result;
	}

	/**
	 * �����մ����ݿ���������Map��������Timestamp��ʽ����ָ����pattern
	 * 
	 * @param target
	 *            Ŀ��map,����һ���Ǹմ����ݿ���������
	 * @param pattern
	 *            ��ʽ�����򣬴�����ȡ
	 */
	@Deprecated
	public static void formatMapDate(Map target, String pattern) {
		for (Object item : target.entrySet()) {
			Map.Entry entry = (Map.Entry) item;
			if (entry.getValue() instanceof Timestamp) {
				SimpleDateFormat sdf = new SimpleDateFormat(pattern);
				entry.setValue(sdf.format((Timestamp) entry.getValue()));
			}
		}
	}

	/**
	 * ����ת��Ϊ��Сд chenjiandong 20090609 add
	 * 
	 * @param date
	 * @param type
	 *            1;2������ʽ1Ϊ�������ģ�2Ϊ��������
	 * @return
	 */
	public static String dataToUpper(Date date, int type) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		int day = ca.get(Calendar.DAY_OF_MONTH);
		return numToUpper(year, type) + "��" + monthToUppder(month, type) + "��" + dayToUppder(day, type) + "��";
	}

	/**
	 * ������ת��Ϊ��д
	 * 
	 * @param num
	 * @param type
	 * @return
	 */
	public static String numToUpper(int num, int type) {// typeΪ��ʽ1;2
		String u1[] = { "��", "һ", "��", "��", "��", "��", "��", "��", "��", "��" };
		String u2[] = { "��", "Ҽ", "��", "��", "��", "��", "½", "��", "��", "��" };
		char[] str = String.valueOf(num).toCharArray();
		String rstr = "";
		if (type == 1) {
			for (int i = 0; i < str.length; i++) {
				rstr = rstr + u1[Integer.parseInt(str[i] + "")];
			}
		} else if (type == 2) {
			for (int i = 0; i < str.length; i++) {
				rstr = rstr + u2[Integer.parseInt(str[i] + "")];
			}
		}
		return rstr;
	}

	/**
	 * ��ת��Ϊ��д
	 * 
	 * @param month
	 * @param type
	 * @return
	 */
	public static String monthToUppder(int month, int type) {
		if (month < 10) {
			return numToUpper(month, type);
		} else if (month == 10) {
			return "ʮ";
		} else {
			return "ʮ" + numToUpper((month - 10), type);
		}
	}

	/**
	 * ��ת��Ϊ��д
	 * 
	 * @param day
	 * @param type
	 * @return
	 */
	public static String dayToUppder(int day, int type) {
		if (day < 20) {
			return monthToUppder(day, type);
		} else {
			char[] str = String.valueOf(day).toCharArray();
			if (str[1] == '0') {
				return numToUpper(Integer.parseInt(str[0] + ""), type) + "ʮ";
			} else {
				return numToUpper(Integer.parseInt(str[0] + ""), type) + "ʮ" + numToUpper(Integer.parseInt(str[1] + ""), type);
			}
		}
	}
}
