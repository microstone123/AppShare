package com.hyk.baseadapter;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.activity.photo.ImagePagerActivity;
import com.hyk.center.CommtentInfo;
import com.hyk.center.DynamicInfo;
import com.hyk.http.Httpaddress;
import com.hyk.resultforjson.ResultStringForLoadap;
import com.hyk.user.StatCommentHandle;
import com.hyk.user.UserInfo;
import com.hyk.utils.AsyncHttpUtil;
import com.hyk.utils.CHString;
import com.hyk.utils.DateUtils;
import com.hyk.utils.JsonBinder;
import com.hyk.utils.ToastUtil;
import com.hyk.view.NestListView;
import com.hyk.view.NewsGridView;
import com.hyk.view.ViewSetClick;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @ClassName: AppCommentAdapter
 * @Description: TODO(用户评论app BaseAdapter)
 * @author linhs
 * @date 2013-12-11 下午3:28:19
 * 
 */
@SuppressLint("HandlerLeak")
public class AppDynamicDateAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	private int item;
	public List<DynamicInfo> userAnswerInfoList;
	private MyScrollHandler scrollHandler;
	private int checkItem = -1;
	// private ScrollHandler nScrollHandler;
	private JsonBinder binder = JsonBinder.buildNonDefaultBinder();
	public Handler handler;
	public String redId;
	public int sendNum;
	public String myRedId;

	public AppDynamicDateAdapter(Context context, List<DynamicInfo> userAnswerInfoList, NestListView recen_gridview,
			MyScrollHandler scrollHandler, int item, Handler handler, String redId, String myRedId) {
		this.context = context;
		this.userAnswerInfoList = userAnswerInfoList;
		this.scrollHandler = scrollHandler;
		this.item = item;
		this.handler = handler;
		inflater = LayoutInflater.from(context);
		this.redId = redId;
		this.myRedId = myRedId;
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<DynamicInfo> questionsForBrief) {
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

	public void addDynamicInfo(DynamicInfo userAnswer) {
		userAnswerInfoList.add(userAnswer);
	}

	/**
	 * 各种ItemView的模板总数
	 */
	@Override
	public int getViewTypeCount() {
		return userAnswerInfoList.size();
	}

	/**
	 * 当前ItemView的模板类型
	 */
	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.the_dynamic_date_item, null);
			holder.the_date_item_tv = (TextView) convertView.findViewById(R.id.the_date_item_tv);
			holder.the_date_conten = (TextView) convertView.findViewById(R.id.the_date_conten);
			holder.praise_num = (TextView) convertView.findViewById(R.id.praise_num);
			holder.the_date_grid = (NewsGridView) convertView.findViewById(R.id.the_date_grid);
			holder.date_comment_list = (NestListView) convertView.findViewById(R.id.date_comment_list);
			holder.reply_btn = (Button) convertView.findViewById(R.id.reply_btn);
			holder.praise_btn = (RelativeLayout) convertView.findViewById(R.id.praise_btn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final DynamicInfo otherDylist = userAnswerInfoList.get(position);
		final List<CommtentInfo> commentDetails = otherDylist.getCommentDetails();
		holder.the_date_item_tv.setText(otherDylist.getCreateTime());
		holder.praise_num.setText(String.valueOf(otherDylist.getPraiseNumber()));
		String content = otherDylist.getContent();
		if (StringUtils.isNotEmpty(content)) {
			holder.the_date_conten.setText(content);
		} else {
			holder.the_date_conten.setVisibility(View.GONE);
		}
		final TextView praiseNum = holder.praise_num;
		final String imgu = otherDylist.getImgUrl();
		final String newsId = String.valueOf(otherDylist.getNewsId());
		holder.the_date_grid.setSelector(R.drawable.hide_listview_yellow_selector);
		if (StringUtils.isNotEmpty(imgu)) {
			holder.the_date_grid.setVisibility(View.VISIBLE);
			final String[] img = imgu.split(";");
			final GridAppListAdapter gridAppListAdapter = new GridAppListAdapter(context, img, true);
			holder.the_date_grid.setAdapter(gridAppListAdapter);
			holder.the_date_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					ViewSetClick.getAlpha(view, context);
					imageBrower(position, img);
				}
			});
		} else {
			holder.the_date_grid.setVisibility(View.GONE);
		}

		final UserCommentAdapter userCommentAdapter = new UserCommentAdapter(context, commentDetails,
				holder.date_comment_list);
		holder.date_comment_list.setAdapter(userCommentAdapter);
		final Button replyBtn = holder.reply_btn;
		final Handler listHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 102:
					Integer redId = msg.getData().getInt("redId");
					String name = msg.getData().getString("nickName");
					String content = msg.getData().getString("content");
					CommtentInfo commtentInfo = new CommtentInfo();
					commtentInfo.setCommentTime(DateUtils.getSysDate("yyyy-MM-dd HH:mm:ss"));
					commtentInfo.setRegId(redId);
					commtentInfo.setNickName(name);
					commtentInfo.setContent(content);
					userCommentAdapter.addCommtentInfo(commtentInfo);
					replyBtn.setBackgroundResource(R.drawable.reply_btn_img_n);
					notifyDataSetChanged();
					// checkItem = -1;
					StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
					// userCommentAdapter.notifyDataSetChanged();
					break;
				case 101:
					checkItem = -1;
					replyBtn.setBackgroundResource(R.drawable.reply_btn_img_n);
					// checkItem = -1;
					StatCommentHandle.getStatCommentHandle().setCheckItem(-1);
					notifyDataSetChanged();
					break;
				case 103:
					int result = msg.getData().getInt("resultInt");
					String resultStr = msg.getData().getString("resultStr");
					if (result == 1) {
						ToastUtil.show(context, resultStr);
					}
					notifyDataSetChanged();
				case 104:
					userCommentAdapter.notifyDataSetChanged();
					break;
				}
			}
		};

		if (StatCommentHandle.getStatCommentHandle().getCheckItem() == position
				&& StatCommentHandle.getStatCommentHandle().getItem() == item + 1) {
			holder.reply_btn.setBackgroundResource(R.drawable.reply_btn_img_p);
		} else {
			holder.reply_btn.setBackgroundResource(R.drawable.reply_btn_img_n);
		}

		holder.reply_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ViewSetClick.getShortAlpha(arg0, context);
				Log.e("checkItem", checkItem+"");
				if (checkItem != position) {
					Log.e("onClick", redId);
					if (StringUtils.isNotEmpty(myRedId)) {
						if (!"0".equals(myRedId)) {
							Message msg = scrollHandler.obtainMessage();
							Bundle b = new Bundle();// 存放数据
							b.putInt("item", item + 1);
							msg.setData(b);
							scrollHandler.sendMessage(msg); // 向Handler发送消息,更新UI

							Message msg1 = handler.obtainMessage();
							Bundle b1 = new Bundle();// 存放数据
							b1.putString("newsId", newsId);
							msg1.setData(b1);
							msg1.what = 10003;
							handler.sendMessage(msg1); // 向Handler发送消息,更新UI
							StatCommentHandle.getStatCommentHandle().setHandler(listHandler);
							StatCommentHandle.getStatCommentHandle().setCheckItem(position);
							StatCommentHandle.getStatCommentHandle().setItem(item + 1);
							checkItem = position;
							replyBtn.setBackgroundResource(R.drawable.reply_btn_img_p);
							notifyDataSetChanged();
						} else {
							ToastUtil.show(context, CHString.NOT_LOGIN);
						}
					} else {
						ToastUtil.show(context, CHString.NOT_LOGIN);
					}
				} else {
					replyBtn.setBackgroundResource(R.drawable.reply_btn_img_n);
					Message msg1 = handler.obtainMessage();
					msg1.what = 10004;
					handler.sendMessage(msg1); // 向Handler发送消息,更新UI
					StatCommentHandle.getStatCommentHandle().setItem(-1);
					checkItem = -1;
				}
			}
		});
		holder.praise_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ViewSetClick.getAlpha(arg0, context);
				if (StringUtils.isNotEmpty(myRedId)) {
					if (!"0".equals(myRedId)) {
						sendPraise(otherDylist.getNewsId(), listHandler);
						praiseNum.setText(String.valueOf((otherDylist.getPraiseNumber() + 1)));
						setData(otherDylist.getNewsId());
						notifyDataSetChanged();
					} else {
						ToastUtil.show(context, CHString.NOT_LOGIN);
					}

				} else {
					ToastUtil.show(context, CHString.NOT_LOGIN);
				}
			}
		});
		return convertView;
	}

	public void setData(int newsId) {
		for (int i = 0; i < userAnswerInfoList.size(); i++) {
			if (userAnswerInfoList.get(i).getNewsId() == newsId) {
				userAnswerInfoList.get(i).setPraiseNumber((userAnswerInfoList.get(i).getPraiseNumber() + 1));
			}
		}
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	class ViewHolder {
		public TextView the_date_item_tv, the_date_conten, praise_num;
		public NewsGridView the_date_grid;
		public NestListView date_comment_list;
		public Button reply_btn;
		public RelativeLayout praise_btn;
	}

	/**
	 * 动态点赞
	 */
	private void sendPraise(final int newsId, final Handler listHandler) {
		String url = Httpaddress.IP_ADDRESS + Httpaddress.NEWS_PRAISE;
		RequestParams params = new RequestParams();
		params.put("newsId", String.valueOf(newsId));
		params.put("imei", UserInfo.getUserInfo().getIMEI());
		params.put("praiseId", redId);
		AsyncHttpUtil.getJson(url, params, new JsonHttpResponseHandler() {
			public void onSuccess(JSONObject arg0) { // 返回的是JSONObject，会调用这里
				ResultStringForLoadap resultString = new ResultStringForLoadap();
				resultString = binder.fromJson(arg0.toString(), ResultStringForLoadap.class);
				if (resultString != null) {
					if (resultString.getResult() != 0) {
						++sendNum;
						if (sendNum <= 4) {
							sendPraise(newsId, listHandler);
						}
					} else {
						Message msg1 = new Message();
						Bundle b1 = new Bundle();// 存放数据
						b1.putInt("resultInt", resultString.getResult());
						b1.putString("resultStr", resultString.getErrMsg());
						msg1.setData(b1);
						msg1.what = 103;
						listHandler.sendMessage(msg1); // 向Handler发送消息,更新UI
					}
				}

			};

			public void onFailure(Throwable arg0) {
				try {
					++sendNum;
					if (sendNum <= 4) {
						sendPraise(newsId, listHandler);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			};

			public void onFinish() {

			};
		});
	}
}
