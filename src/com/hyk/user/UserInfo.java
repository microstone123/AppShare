package com.hyk.user;

public class UserInfo {
	private static UserInfo userInfo = new UserInfo();

	/**
	 * 用户Id
	 */
	private String userId;

	/**
	 * 经度
	 */
	private Double longitude;
	/**
	 * 纬度
	 */
	private Double latitude;
	/**
	 * 最后一次定位时间
	 */
	private String laterTime;
	/**
	 * 手机IMSI号
	 */
	private String IMSI;
	/**
	 * 手机IMEI号
	 */
	private String IMEI;
	/**
	 * 手机型号
	 */
	private String phoneType;
	/**
	 * App版本号
	 */
	private String appVersion;
	/**
	 * 定位精度半径，单位是米
	 */
	private float radius;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 区/县
	 */
	private String district;
	/**
	 * 地址
	 */
	private String addrStr;
	/**
	 * 城市编码
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
