package com.hyk.app;

import java.io.Serializable;

@SuppressWarnings("serial")
public class InfoOfApp implements Serializable {
	/**
	 * appid
	 */
	public String id;
	/**
	 * 名称
	 */
	public String name;
	/**
	 * 应用图标路径
	 */
	public String appImage;
	/**
	 * 是否免费
	 */
	public int isFree;
	/**
	 * 是否通过安全检测 //0未经过检测，1安全(已通过安全检测)
	 */
	public int isSafe;

	/**
	 * 0不含，1含插件或广告
	 */
	public int isAd;
	/**
	 * 
	 */
	private String adcontent;
	/**
	 * 评分
	 */
	public Float score;
	/**
	 * 评分人数
	 */
	public Long participants;
	/**
	 * 大小(MB)
	 */
	public String paramsSize;
	/**
	 * 版本
	 */
	public String paramsVname;
	/**
	 * 下载次数
	 */
	public Long downNum;
	/**
	 * 更新时间
	 */
	public String paramsUpdateTime;
	/**
	 * 系统要求,”<br/>
	 * ”为换行符
	 */
	public String paramsPlatForm;
	/**
	 * 
	 */
	private String md5str;
	/**
	 * 下载地址
	 */
	public String downUrl;
	/**
	 * 编辑评语
	 */
	public String rditorReviews;
	/**
	 * 介绍:”<br/>
	 * ”为换行符
	 */
	public String briefIntroduction;
	/**
	 * 类别
	 */
	public String paramsCatename;

	/**
	 * 图片地址集合，以”,”隔开
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
