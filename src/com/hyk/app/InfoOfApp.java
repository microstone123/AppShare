package com.hyk.app;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InfoOfApp implements Serializable {
	/**
	 * appid
	 */
	public String id;
	/**
	 * ����
	 */
	public String name;
	/**
	 * Ӧ��ͼ��·��
	 */
	public String appImage;
	/**
	 * �Ƿ����
	 */
	public int isFree;
	/**
	 * �Ƿ�ͨ����ȫ��� //0δ������⣬1��ȫ(��ͨ����ȫ���)
	 */
	public int isSafe;

	/**
	 * 0������1���������
	 */
	public int isAd;
	/**
	 * 
	 */
	private String adcontent;
	/**
	 * ����
	 */
	public Float score;
	/**
	 * ��������
	 */
	public Long participants;
	/**
	 * ��С(MB)
	 */
	public String paramsSize;
	/**
	 * �汾
	 */
	public String paramsVname;
	/**
	 * ���ش���
	 */
	public Long downNum;
	/**
	 * ����ʱ��
	 */
	public String paramsUpdateTime;
	/**
	 * ϵͳҪ��,��<br/>
	 * ��Ϊ���з�
	 */
	public String paramsPlatForm;
	/**
	 * 
	 */
	private String md5str;
	/**
	 * ���ص�ַ
	 */
	public String downUrl;
	/**
	 * �༭����
	 */
	public String rditorReviews;
	/**
	 * ����:��<br/>
	 * ��Ϊ���з�
	 */
	public String briefIntroduction;
	/**
	 * ���
	 */
	public String paramsCatename;

	/**
	 * ͼƬ��ַ���ϣ��ԡ�,������
	 */
	public String imgUrls;

	public String crawlertime;

	public String websource;
	public String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppImage() {
		return appImage;
	}

	public void setAppImage(String appImage) {
		this.appImage = appImage;
	}

	public int getIsFree() {
		return isFree;
	}

	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}

	public int getIsSafe() {
		return isSafe;
	}

	public void setIsSafe(int isSafe) {
		this.isSafe = isSafe;
	}

	public int getIsAd() {
		return isAd;
	}

	public void setIsAd(int isAd) {
		this.isAd = isAd;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Long getParticipants() {
		return participants;
	}

	public void setParticipants(Long participants) {
		this.participants = participants;
	}

	public String getParamsSize() {
		return paramsSize;
	}

	public void setParamsSize(String paramsSize) {
		this.paramsSize = paramsSize;
	}

	public String getParamsVname() {
		return paramsVname;
	}

	public void setParamsVname(String paramsVname) {
		this.paramsVname = paramsVname;
	}

	public Long getDownNum() {
		return downNum;
	}

	public void setDownNum(Long downNum) {
		this.downNum = downNum;
	}

	public String getParamsUpdateTime() {
		return paramsUpdateTime;
	}

	public void setParamsUpdateTime(String paramsUpdateTime) {
		this.paramsUpdateTime = paramsUpdateTime;
	}

	public String getParamsPlatForm() {
		return paramsPlatForm;
	}

	public void setParamsPlatForm(String paramsPlatForm) {
		this.paramsPlatForm = paramsPlatForm;
	}

	public String getParamsCatename() {
		return paramsCatename;
	}

	public void setParamsCatename(String paramsCatename) {
		this.paramsCatename = paramsCatename;
	}

	public String getRditorReviews() {
		return rditorReviews;
	}

	public void setRditorReviews(String rditorReviews) {
		this.rditorReviews = rditorReviews;
	}

	public String getBriefIntroduction() {
		return briefIntroduction;
	}

	public void setBriefIntroduction(String briefIntroduction) {
		this.briefIntroduction = briefIntroduction;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public String getDownUrl() {
		return downUrl;
	}

	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAdcontent() {
		return adcontent;
	}

	public void setAdcontent(String adcontent) {
		this.adcontent = adcontent;
	}

	public String getMd5str() {
		return md5str;
	}

	public void setMd5str(String md5str) {
		this.md5str = md5str;
	}

	public String getCrawlertime() {
		return crawlertime;
	}

	public void setCrawlertime(String crawlertime) {
		this.crawlertime = crawlertime;
	}

	public String getWebsource() {
		return websource;
	}

	public void setWebsource(String websource) {
		this.websource = websource;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
