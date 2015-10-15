package com.hyk.center;

import java.util.List;

/**
 * @ClassName: DynamicInfo
 * @Description: TODO(动态)
 * @author linhs
 * @date 2014-3-6 下午6:45:36
 */
public class DynamicInfo {

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 评论个数
	 */
	private Integer commentNum;

	/**
	 * 分享个数
	 */
	private Integer shareNum;

	/**
	 * 时间
	 */
	private String createTime;

	/**
	 * 动态ID
	 */
	private Integer newsId;

	/**
	 * 图片
	 */
	private String imgUrl;

	/**
	 * 名称
	 */
	private String userName;

	/**
	 * 头像
	 */
	private String headPic;

	/**
	 * 注册Id
	 */
	private Integer ownerId;

	/**
	 * 赞个数
	 */
	private int praiseNumber;
	
	/**
	 * imei
	 */
	private String imei;
	
	/**
	 * 评论列表
	 */
	private List<CommtentInfo> commentDetails;

	public String getContent() {
		return content;
	}

	public int getPraiseNumber() {
		return praiseNumber;
	}

	public void setPraiseNumber(int praiseNumber) {
		this.praiseNumber = praiseNumber;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getShareNum() {
		return shareNum;
	}

	public void setShareNum(Integer shareNum) {
		this.shareNum = shareNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getNewsId() {
		return newsId;
	}

	public void setNewsId(Integer newsId) {
		this.newsId = newsId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public List<CommtentInfo> getCommentDetails() {
		return commentDetails;
	}

	public void setCommentDetails(List<CommtentInfo> commentDetails) {
		this.commentDetails = commentDetails;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

}
