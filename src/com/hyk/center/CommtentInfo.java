package com.hyk.center;


/**
 * @ClassName: CommtentInfo
 * @Description: TODO(��̬����)
 * @author linhs
 * @date 2014-3-6 ����6:39:40
 */
public class CommtentInfo {
	
	/**
	 * ע��ID
	 */
	private Integer regId;
	
	/**
	 * �ǳ�
	 */
	private String nickName;
	
	/**
	 * ͷ��
	 */
	private String headPic;
	
	/**
	 * ʱ��
	 */
	private String commentTime;
	
	/**
	 * ����
	 */
	private String content;

	public Integer getRegId() {
		return regId;
	}

	public void setRegId(Integer regId) {
		this.regId = regId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadPic() {
		return headPic;
	}

	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
