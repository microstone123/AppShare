package com.hyk.baseadapter;

import java.util.HashMap;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.hyk.activity.R;
import com.hyk.center.OtherDylist;
import com.hyk.view.NestListView;

/**
 * @ClassName: AppCommentAdapter
 * @Description: TODO(用户评论app BaseAdapter)
 * @author linhs
 * @date 2013-12-11 下午3:28:19
 * 
 */
@SuppressLint("UseSparseArrays")
public class AppDynamicAdapter extends BaseAdapter {
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	private Context context;
	private LayoutInflater inflater = null;

	public List<OtherDylist> userAnswerInfoList;
	public MyScrollHandler scrollHandler;
	public Handler handler;
	public String redId;
	public String myRedId;

	// public ScrollHandler nScrollHandler;

	public AppDynamicAdapter(Context context, List<OtherDylist> userAnswerInfoList, MyScrollHandler scrollHandler,
			Handler handler, String redId,String myRedId) {
		this.context = context;
		this.scrollHandler = scrollHandler;
		this.userAnswerInfoList = userAnswerInfoList;
		inflater = LayoutInflater.from(context);
		this.handler = handler;
		this.redId = redId;
		this.myRedId = myRedId;
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<OtherDylist> questionsForBrief) {
		this.userAnswerInfoList = questionsForBrief;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (userAnswerInfoList.size() == 0) {
			return 1;
		} else {
			return userAnswerInfoList.size();
		}
	}

	@Override
	public Object getItem(int i) {
		return null;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	public void addOtherDylist(OtherDylist userAnswer) {
		userAnswerInfoList.add(userAnswer);
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

	/**
	 * 各种ItemView的模板总数
	 */
	@Override
	public int getViewTypeCount() {
		if (userAnswerInfoList.size() == 0) {
			return 1;
		} else {
			return userAnswerInfoList.size();
		}
	}

	// /**
	// * 当前ItemView的模板类型
	// */
	// @Override
	// public int getItemViewType(int position) {
	// return position;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (lmap.get(position) == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.the_dynamic_item, null);
			holder.the_date_tv = (TextView) convertView.findViewById(R.id.the_date_tv);
			holder.date_list = (NestListView) convertView.findViewById(R.id.date_list);
			holder.dy_img = (ImageView) convertView.findViewById(R.id.dy_img);
			lmap.put(position, convertView);
			convertView.setTag(holder);
			if (userAnswerInfoList.size() != 0) {
				final OtherDylist otherDylist = userAnswerInfoList.get(position);
				holder.the_date_tv.setText(otherDylist.getMakeTime());
				// nScrollHandler = new ScrollHandler(holder.date_list);
				final AppDynamicDateAdapter appDynamicDateAdapter = new AppDynamicDateAdapter(context,
						otherDylist.getDayNewsList(), holder.date_list, scrollHandler, position, handler, redId,myRedId);
				holder.date_list.setAdapter(appDynamicDateAdapter);

				if (position == 0) {
					holder.dy_img.setVisibility(View.INVISIBLE);
				} else {
					holder.dy_img.setVisibility(View.VISIBLE);
				}
			}
		} else {
			convertView = lmap.get(position);
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	class ViewHolder {
		public TextView the_date_tv;
		public NestListView date_list;
		public ImageView dy_img;
	}
}
