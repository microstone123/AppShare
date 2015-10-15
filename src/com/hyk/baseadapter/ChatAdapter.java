package com.hyk.baseadapter;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.utils.CHString;
import com.hyk.utils.DateUtils;
import com.hyk.utils.ExpressionUtil;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.view.RoundImageView;
import com.hyk.xmpp.model.Msg;
import com.hyk.xmpp.model.OneFridenMessages;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ChatAdapter extends BaseAdapter {
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	private Context cxt;
	private LayoutInflater inflater;
	OneFridenMessages friendMessageBean;
	private int SCALE_SIZE = 5; // 足够大则会撑满屏幕
	private Drawable myBitmap;
	private String friendImg;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private String myurl;
	private String time;
	private String pFRIENDID;

	public ChatAdapter(Context context, OneFridenMessages friendMessageBean, Drawable myBitmap, String friendImg,
			String myurl, String pFRIENDID) {
		this.cxt = context;
		this.myBitmap = myBitmap;
		this.friendImg = friendImg;
		inflater = LayoutInflater.from(context);
		this.friendMessageBean = friendMessageBean;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = ImageLoaderPartner.getOptions(R.drawable.the_def_img);
		this.myurl = myurl;
		this.pFRIENDID = pFRIENDID;
	}

	@Override
	public int getCount() {
		int count = 0;
		try {
			count = friendMessageBean.MessageList.size();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return count;
	}

	public void setData(OneFridenMessages friendMessageBean) {
		this.friendMessageBean = friendMessageBean;
		notifyDataSetChanged();
	}

	public OneFridenMessages getData() {
		return friendMessageBean;
	}

	@Override
	public Object getItem(int position) {
		return friendMessageBean.MessageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Msg nowMsg = friendMessageBean.MessageList.get(position);
		if (lmap.get(position) == null) {
			holder = new ViewHolder();
			if (nowMsg.getInOrOut().equals("IN")) {
				convertView = inflater.inflate(R.layout.formclient_chat_in, null);
				holder.friend_img = (RoundImageView) convertView.findViewById(R.id.friend_img);
			} else {
				convertView = inflater.inflate(R.layout.formclient_chat_out, null);
				holder.my_img = (RoundImageView) convertView.findViewById(R.id.my_img);
			}

			holder.formclient_row_msg = (TextView) convertView.findViewById(R.id.formclient_row_msg);
			holder.chat_time_text = (TextView) convertView.findViewById(R.id.chat_time_text);
			lmap.put(position, convertView);
			convertView.setTag(holder);

			if (!nowMsg.getInOrOut().equals("IN")) {
				if (StringUtils.isNotEmpty(myurl)) {
					imageLoader.displayImage(myurl, holder.my_img, options);
				} else {
					holder.my_img.setImageDrawable(myBitmap);
				}
			} else {
				imageLoader.displayImage(friendImg, holder.friend_img, options);
			}

			String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
			try {

				if (StringUtils.isEmpty(time)) {
					time = nowMsg.getSendDate();
					holder.chat_time_text.setVisibility(View.VISIBLE);
					holder.chat_time_text.setText(nowMsg.getSendDate());
				} else {
					if (DateUtils.getForMinuteDiff(time, nowMsg.getSendDate()) > 3) {
						holder.chat_time_text.setVisibility(View.VISIBLE);
						holder.chat_time_text.setText(nowMsg.getSendDate());
						time = nowMsg.getSendDate();
					} else {
						holder.chat_time_text.setVisibility(View.GONE);
					}
				}

				String newStr = CHString.checkMark(nowMsg.getMsg());
				SpannableString spannableString = ExpressionUtil.getExpressionString(cxt, newStr, zhengze);
				holder.formclient_row_msg.setText(spannableString);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			convertView = lmap.get(position);
			holder = (ViewHolder) convertView.getTag();
		}

		// try {
		//
		// // 新消息设为0
		// XmppApplication.AllFriendsMessageMapData.get(pFRIENDID).NewMessageCount
		// = 0;
		// XmppApplication.sharedPreferences.edit().putInt(pFRIENDID +
		// XmppApplication.user, 0).commit();
		// XmppApplication.sharedPreferenceNews.edit().putString(pFRIENDID,
		// "").commit();
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
		return convertView;
	}

	class ViewHolder {
		public RoundImageView my_img, friend_img;
		public TextView formclient_row_msg, chat_time_text;

	}
}
