package com.hyk.app;

import java.util.List;

/**
 * @ClassName: APPComment
 * @Description: TODO(app �û�����)
 * @author linhs
 * @date 2013-12-23 ����5:02:16
 */
public class APPCommentList {
	/**
	 * ��������
	 */
	private int total;
	/**
	 * ��������
	 */
	private List<APPComment> list;

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<APPComment> getList() {
		return list;
	}

	public void setList(List<APPComment> list) {
		this.list = list;
	}

}
