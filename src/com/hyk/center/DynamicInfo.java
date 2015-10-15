package com.hyk.center;

import java.util.List;

/**
 * @ClassName: DynamicInfo
 * @Description: TODO(��̬)
 * @author linhs
 * @date 2014-3-6 ����6:45:36
 */
public class DynamicInfo {

	/**
	 * ����
	 */
	private String content;

	/**
	 * ���۸���
	 */
	private Integer commentNum;

	/**
	 * �������
	 */
	private Integer shareNum;

	/**
	 * ʱ��
	 */
	private String createTime;

	/**
	 * ��̬ID
	 */
	private Integer newsId;

	/**
	 * ͼƬ
	 */
	private String imgUrl;

	/**
	 * ����
	 */
	private String userName;

	/**
	 * ͷ��
	 */
	private String headPic;

	/**
	 * ע��Id
	 */
	private Integer ownerId;

	/**
	 * �޸���
	 */
	private int praiseNumber;
	
	/**
	 * imei
	 */
	private String imei;
	
	/**
	 * �����б�
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
