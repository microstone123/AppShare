package com.hyk.app;

/**
 * @ClassName: APPComment
 * @Description: TODO(app �û�����)
 * @author linhs
 * @date 2013-12-23 ����5:02:16
 */
public class APPComment {
	/**
	 * ����id
	 */
	private int id;
	/**
	 * �û�
	 */
	private String name;
	/**
	 * ����
	 */
	private String score;
	/**
	 * app�汾
	 */
	private String version;
	/**
	 * ����
	 */
	private String content;
	/**
	 * ����ʱ��
	 */
	private String sendTime;

	/**
	 * �ƶ��豸
	 */
	private String machine;
	/**
	 * appid
	 */
	private int appid;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getMachine() {
		return machine;
	}

	public void setMachine(String machine) {
		this.machine = machine;
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}
}
