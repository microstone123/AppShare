package com.hyk.download;

import android.annotation.SuppressLint;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FROMATE_DEF = "yyyy-MM-dd HH:mm:ss";
	public static Calendar calendar = null;

	/**
	 * ����ʱ���ַ� ���ָ�� str ʱ���ַ� Ĭ�ϸ�ʽ "yyyy-MM-dd HH:mm:ss" sormatStr ת��Ϊ�ĸ�ʽ
	 */
	public static String formatDateToStr(String str, String sormatStr) {
		DateFormat sf = new SimpleDateFormat(TIME_FROMATE_DEF);
		try {
			Date date = sf.parse(str);
			if (date == null)
				return "";
			DateFormat sfd = new SimpleDateFormat(sormatStr);
			return sfd.format(date);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(Date date) {
		if (date == null)
			return "";
		DateFormat sf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return sf.format(date);
	}

	public static Date parseDate(String str) {
		DateFormat sf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		try {
			return sf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String formatDate(Date date, String parrent) {
		if (date == null)
			return "";
		if (parrent == null || "".equals(parrent)) {
			return formatDate(date);
		} else {
			DateFormat sf = new SimpleDateFormat(parrent);
			return sf.format(date);
		}
	}

	public static String formatDate(Timestamp date, String parrent) {
		if (date == null)
			return "";
		if (parrent == null || "".equals(parrent)) {
			parrent = "yyyy-MM-dd";
		}
		DateFormat sf = new SimpleDateFormat(parrent);
		return sf.format(date);
	}

	public static Date parseDate(String str, String parrent) {
		if (parrent == null || "".equals(parrent))
			return parseDate(str);
		DateFormat sf = new SimpleDateFormat(parrent);
		try {
			return sf.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * �ж������ڵĲ��Ƿ񳬹�1�� 0Ϊδ������1Ϊ����
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffDateGreaterOneDay(Date date1, Date date2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		if (betweenYears > 0) {
			return 1;
		} else if (betweenYears == 0) {
			int betweenMonth = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
			if (betweenMonth > 0) {
				return 1;
			} else if (betweenMonth == 0) {
				return (c2.get(Calendar.DAY_OF_MONTH) - c1.get(Calendar.DAY_OF_MONTH)) > 0 ? 1 : 0;
			}
		}
		return 0;
	}

	/**
	 * ������ʱ������������
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int diffDate(Date date1, Date date2) {
		int diffDay = 0;
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(date1);
		c2.setTime(date2);
		// ��֤�ڶ���ʱ��һ�����ڵ�һ��ʱ��
		if (c1.after(date2)) {
			c1 = c2;
			c2.setTime(date1);
		}
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		diffDay = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			diffDay += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return diffDay;
	}

	/**
	 * ���ݳ������ڼ�������
	 * 
	 * @param birthDay
	 * @return δ�����ڷ���0
	 * @throws Exception
	 */
	public static int getAge(Date birthDay) {

		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay)) {
			return 0;
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}

		return age;
	}

	/**
	 * ���ݳ������ڼ�������
	 * 
	 * @param strBirthDay
	 *            �ַ���������
	 * @param format
	 *            ���ڸ�ʽ
	 * @return δ�����ڷ���0
	 * @throws Exception
	 */
	public static int getAge(String strBirthDay, String format) {

		DateFormat df = new SimpleDateFormat(format);
		Date birthDay = null;
		try {
			birthDay = df.parse(strBirthDay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return birthDay == null ? 0 : getAge(birthDay);
	}

	public static Date addDate(Date date, String time) {
		if (date == null)
			return null;
		if (time == null || "".equals(time) || time.length() < 2)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String unit = time.substring(time.length() - 1);
		if ("y".equalsIgnoreCase(unit)) {
			cal.add(Calendar.YEAR, Integer.parseInt(time.substring(0, time.length() - 1)));
		} else if ("m".equalsIgnoreCase(unit)) {
			cal.add(Calendar.MONTH, Integer.parseInt(time.substring(0, time.length() - 1)));
		}
		return cal.getTime();
	}

	/**
	 * �����������
	 * 
	 * @return
	 */
	public static String getYesterday() {
		Date date = new Date();
		date = new Date(date.getTime() - 1000 * 60 * 60 * 24);
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
		return dateFm.format(date);
	}

	/**
	 * ��������ڵ�ʱ���
	 * 
	 * @param date1
	 * @param date2
	 * @param type
	 *            "hour","min","sec","ms"
	 * @return
	 */
	public static long getDiffTime(String date1, String date2, String type) {
		long ret = 0;
		try {
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date begin = dfs.parse(date1);
			Date end = dfs.parse(date2);
			long between = (end.getTime() - begin.getTime()) / 1000;// ����1000��Ϊ��ת������
			if ("ms".equalsIgnoreCase(type)) {
				ret = between * 1000;
			} else if ("sec".equalsIgnoreCase(type)) {
				ret = between;
			} else if ("min".equalsIgnoreCase(type)) {
				ret = between / 60;
			} else if ("hour".equalsIgnoreCase(type)) {
				ret = between / (60 * 60);
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * ��ý�����һ�ܵ��ܼ�
	 * 
	 * @return
	 */
	public static String getCurDayOfWeek() {
		// ��ý�����һ�ܵ��ܼ���������(0)��ʼ
		GregorianCalendar obj = new GregorianCalendar();
		obj.setTime(new java.util.Date());
		int week = obj.get(GregorianCalendar.DAY_OF_WEEK) - 1;
		String dayOfWeek = "";
		switch (week) {
		case 0:
			dayOfWeek = "����";
			break;
		case 1:
			dayOfWeek = "��һ";
			break;
		case 2:
			dayOfWeek = "�ܶ�";
			break;
		case 3:
			dayOfWeek = "����";
			break;
		case 4:
			dayOfWeek = "����";
			break;
		case 5:
			dayOfWeek = "����";
			break;
		case 6:
			dayOfWeek = "����";
			break;
		default:
			break;
		}
		return dayOfWeek;
	}

	/**
	 * ���ش������ڵ�����
	 * 
	 * @param s
	 * @return
	 */
	public static String getDayOfWeek(String s) {
		final String dayNames[] = { "����", "��һ", "�ܶ�", "����", "����", "����", "����" };
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = sdfInput.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0) {
			dayOfWeek = 0;
		}
		return (dayNames[dayOfWeek]);
	}

	/**
	 * ��ȡϵͳʱ��
	 */
	public static String getSysDate(String dateFor) {
		Date curDate = new Date(System.currentTimeMillis());// ��ȡ��ǰʱ��
		SimpleDateFormat dfs = new SimpleDateFormat(dateFor);
		return dfs.format(curDate);
	}

	/**
	 * ������������������������
	 */
	public static int getForMinuteDiff(Date d1, Date d2) {
		int t = 0;
		if (d1 != null) {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(d1);
			c2.setTime(d2);
			t = c2.get(Calendar.MINUTE) - c1.get(Calendar.MINUTE);
		}
		return t;
	}

	/**
	 * �����������������
	 * 
	 * @param date
	 *            Date ����
	 * @return �������
	 */
	public static int getYear(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * ���������������·�
	 * 
	 * @param date
	 *            Date ����
	 * @return �����·�
	 */
	public static int getMonth(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * ���������������շ�
	 * 
	 * @param date
	 *            Date ����
	 * @return �����շ�
	 */
	public static int getDay(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * ��������������Сʱ
	 * 
	 * @param date
	 *            ����
	 * @return ����Сʱ
	 */
	public static int getHour(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * �������������ط���
	 * 
	 * @param date
	 *            ����
	 * @return ���ط���
	 */
	public static int getMinute(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * ��������
	 * 
	 * @param date
	 *            Date ����
	 * @return ��������
	 */
	public static int getSecond(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * �������������غ���
	 * 
	 * @param date
	 *            ����
	 * @return ���غ���
	 */
	public static long getMillis(Date date) {
		calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

	// public static void main(String[] args) {
	// System.out.println(getSysDate());
	// }
}
