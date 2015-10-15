package com.hyk.center;


/**
 * @ClassName: CommtentInfo
 * @Description: TODO(动态评论)
 * @author linhs
 * @date 2014-3-6 下午6:39:40
 */
public class CommtentInfo {
	
	/**
	 * 注册ID
	 */
	private Integer regId;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 头像
	 */
	private String headPic;
	
	/**
	 * 时间
	 */
	private String commentTime;
	
	/**
	 * 内容
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
