package com.hyk.app;

import java.util.List;

/**
 * @ClassName: APPComment
 * @Description: TODO(app 用户评论)
 * @author linhs
 * @date 2013-12-23 下午5:02:16
 */
public class APPCommentList {
	/**
	 * 评论总数
	 */
	private int total;
	/**
	 * 评论总数
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
