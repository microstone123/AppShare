package com.hyk.baseadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.app.APPComment;
import com.hyk.utils.CHString;

/**
 * @ClassName: AppCommentAdapter
 * @Description: TODO(用户评论app BaseAdapter)
 * @author linhs
 * @date 2013-12-11 下午3:28:19
 * 
 */
public class AppCommentAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;

	public List<APPComment> userAnswerInfoList;

	public AppCommentAdapter(Context context, List<APPComment> userAnswerInfoList) {
		// this.context = context;
		this.userAnswerInfoList = userAnswerInfoList;
		inflater = LayoutInflater.from(context);
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<APPComment> questionsForBrief) {
		this.userAnswerInfoList = questionsForBrief;
	}

	@Override
	public int getCount() {
		return userAnswerInfoList.size();
	}

	@Override
	public Object getItem(int i) {
		return userAnswerInfoList.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void addAPPComment(APPComment userAnswer) {
		userAnswerInfoList.add(userAnswer);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.app_comment_item, null);
			holder.user_name = (TextView) convertView.findViewById(R.id.user_comment_name);
			holder.text_answer_time = (TextView) convertView.findViewById(R.id.text_comment_time);
			holder.answer_content = (TextView) convertView.findViewById(R.id.comment_content);
						
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		APPComment userAnswerInfo = new APPComment();
		userAnswerInfo = userAnswerInfoList.get(position);
		holder.user_name.setText(userAnswerInfo.getName());
		holder.text_answer_time.setText(userAnswerInfo.getSendTime());
		holder.answer_content.setText(userAnswerInfo.getContent());

		return convertView;
	}

	class ViewHolder {
		public TextView user_name;
		public TextView text_answer_time;
		public TextView answer_content;
	}
}
