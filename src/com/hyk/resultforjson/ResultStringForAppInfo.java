package com.hyk.resultforjson;

import com.hyk.app.InfoOfApp;


public class ResultStringForAppInfo {
	/**
	 * 0Ϊ�ɹ���1Ϊʧ�ܣ�
	 */
	private int result;
	/**
	 * ������Ϣ
	 */
	private String errMsg;
	/**
	 * ����Id
	 */
	private String errId;
	/**
	 * Ϊǩ���ַ�����������
	 */
	private String sign;
	/**
	 * 
	 */
	private String errCode;
	/**
	 * 
	 */
	private String infor;
	/**
	 * 
	 */
	private String sucMsg;
	/**
	 * �ɹ����ص�������Ϣ����result=0ʱ��Ч
	 */
	private InfoOfApp obj;
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getErrId() {
		return errId;
	}
	public void setErrId(String errId) {
		this.errId = errId;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getInfor() {
		return infor;
	}
	public void setInfor(String infor) {
		this.infor = infor;
	}
	public String getSucMsg() {
		return sucMsg;
	}
	public void setSucMsg(String sucMsg) {
		this.sucMsg = sucMsg;
	}
	public InfoOfApp getObj() {
		return obj;
	}
	public void setObj(InfoOfApp obj) {
		this.obj = obj;
	}
	

}
