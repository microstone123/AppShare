package com.hyk.baseadapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.center.CommtentInfo;
import com.hyk.utils.ExpressionUtil;
import com.hyk.view.NestListView;

/**
 * @ClassName: AppCommentAdapter
 * @Description: TODO(用户评论app BaseAdapter)
 * @author linhs
 * @date 2013-12-11 下午3:28:19
 * 
 */
public class UserCommentAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	public List<CommtentInfo> commtentInfo;
	public NestListView commentList;

	public UserCommentAdapter(Context context, List<CommtentInfo> commtentInfo, NestListView commentList) {
		this.context = context;
		this.commtentInfo = commtentInfo;
		inflater = LayoutInflater.from(context);
		this.commentList = commentList;
		if (commtentInfo.size() == 0) {
			this.commentList.setVisibility(View.INVISIBLE);
		}
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<CommtentInfo> commtentInfo) {
		this.commtentInfo = commtentInfo;
	}

	@Override
	public int getCount() {
		return commtentInfo.size();
	}

	@Override
	public Object getItem(int i) {
		return commtentInfo.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void addCommtentInfo(CommtentInfo userAnswer) {
		if (this.commentList.getVisibility() == View.INVISIBLE) {
			this.commentList.setVisibility(View.VISIBLE);
		}
		commtentInfo.add(userAnswer);
		notifyDataSetChanged();
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.user_comment_item, null);
			holder.user_name = (TextView) convertView.findViewById(R.id.usercomment_name);
			holder.text_content = (TextView) convertView.findViewById(R.id.text_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final CommtentInfo userAnswerInfo = commtentInfo.get(position);
		holder.user_name.setText(userAnswerInfo.getNickName() + " ：");
		String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
		try {
			SpannableString spannableString = ExpressionUtil.getExpressionString(context, userAnswerInfo.getContent(), zhengze);
			holder.text_content.setText(spannableString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder {
		public TextView user_name;
		public TextView text_content;
	}
}
