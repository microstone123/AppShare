package com.hyk.user;

public class UserInfo {
	private static UserInfo userInfo = new UserInfo();

	/**
	 * �û�Id
	 */
	private String userId;

	/**
	 * ����
	 */
	private Double longitude;
	/**
	 * γ��
	 */
	private Double latitude;
	/**
	 * ���һ�ζ�λʱ��
	 */
	private String laterTime;
	/**
	 * �ֻ�IMSI��
	 */
	private String IMSI;
	/**
	 * �ֻ�IMEI��
	 */
	private String IMEI;
	/**
	 * �ֻ��ͺ�
	 */
	private String phoneType;
	/**
	 * App�汾��
	 */
	private String appVersion;
	/**
	 * ��λ���Ȱ뾶����λ����
	 */
	private float radius;
	/**
	 * ʡ
	 */
	private String province;
	/**
	 * ����
	 */
	private String city;
	/**
	 * ��/��
	 */
	private String district;
	/**
	 * ��ַ
	 */
	private String addrStr;
	/**
	 * ���б���
	 */
	private String cityCode;

	public static UserInfo getUserInfo() {
		return userInfo;
	}

	public static void setUserInfo(UserInfo userInfo) {
		UserInfo.userInfo = userInfo;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLaterTime() {
		return laterTime;
	}

	public void setLaterTime(String laterTime) {
		this.laterTime = laterTime;
	}

	public String getIMSI() {
		return IMSI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAddrStr() {
		return addrStr;
	}

	public void setAddrStr(String addrStr) {
		this.addrStr = addrStr;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
