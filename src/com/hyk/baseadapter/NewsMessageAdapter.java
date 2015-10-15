package com.hyk.baseadapter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.dialog.DetailsDiao;
import com.hyk.news.NewsForInfo;
import com.hyk.utils.CHString;
import com.hyk.utils.DateUtils;
import com.hyk.utils.ExpressionUtil;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.view.RoundImageViewExtNetw;
import com.hyk.view.ViewSetClick;
import com.hyk.xmpp.XmppApplication;
import com.hyk.xmpp.XmppService;

/**
 * @ClassName: AppCommentAdapter
 * @Description: TODO(用户评论app BaseAdapter)
 * @author linhs
 * @date 2013-12-11 下午3:28:19
 * 
 */
public class NewsMessageAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater = null;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();
	// protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	public List<NewsForInfo> userAnswerInfoList;
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;
	private String regId;

	public NewsMessageAdapter(Context context, List<NewsForInfo> userAnswerInfoList, String regId) {
		this.context = context;
		this.userAnswerInfoList = userAnswerInfoList;
		inflater = LayoutInflater.from(context);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.the_def_img);
		this.regId = regId;
		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
	}

	// setData()要在MyAdapter类里设置，用于设置数据源
	public void setData(List<NewsForInfo> questionsForBrief) {
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

	public void addNewsForInfo(NewsForInfo userAnswer) {
		userAnswerInfoList.add(userAnswer);
	}

	public NewsForInfo getData(int position) {
		return userAnswerInfoList.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.news_message_item, null);
			holder.the_user_img = (RoundImageViewExtNetw) convertView.findViewById(R.id.the_user_img);
			holder.the_news_name = (TextView) convertView.findViewById(R.id.the_news_name);
			holder.the_news_content = (TextView) convertView.findViewById(R.id.the_news_content);
			holder.news_time = (TextView) convertView.findViewById(R.id.news_time);
			holder.the_app_img = (NetworkImageView) convertView.findViewById(R.id.the_app_img);
			holder.news_num = (TextView) convertView.findViewById(R.id.news_num);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		NewsForInfo userAnswerInfo = new NewsForInfo();
		final NewsForInfo userAnswerInfo = userAnswerInfoList.get(position);
		
		
//		imageLoader.displayImage(userAnswerInfo.getHeadPic(), holder.the_user_img, options);
		
		holder.the_user_img.setErrorImageResId(R.drawable.the_def_img);
		holder.the_user_img.setDefaultImageResId(R.drawable.the_def_img);
		holder.the_user_img.setImageUrl(userAnswerInfo.getHeadPic(), XmppApplication.getsInstance().imageLoader);
		
		holder.the_news_name.setText(userAnswerInfo.getUserName());

		holder.news_time.setText(DateUtils.formatDateToStr(userAnswerInfo.getTime(), "MM-dd HH:mm:ss"));

		try {
			int newCount = XmppApplication.sharedPreferences.getInt(
					XmppService.getFullUsername(String.valueOf(userAnswerInfo.getRegId())) + XmppApplication.user, 0);

			String news = XmppApplication.sharedPreferenceNews.getString(
					XmppService.getFullUsername(String.valueOf(userAnswerInfo.getRegId())), "");

			if (newCount != 0) {
				holder.news_num.setVisibility(View.VISIBLE);
				holder.news_num.setText(String.valueOf(newCount));
			} else {
				holder.news_num.setVisibility(View.INVISIBLE);
			}

			if (StringUtils.isNotEmpty(userAnswerInfo.getAppImage())) {
//				imageLoader.displayImage(userAnswerInfo.getAppImage(), holder.the_app_img, options);
				
				holder.the_app_img.setErrorImageResId(R.drawable.transparent_d);
				holder.the_app_img.setDefaultImageResId(R.drawable.transparent_d);
				holder.the_app_img.setImageUrl(userAnswerInfo.getAppImage(), XmppApplication.getsInstance().imageLoader);
				
				
				holder.the_news_content.setText(CHString.I_DOWN_APP);

				final int appId = userAnswerInfo.getAppId();

				holder.the_app_img.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						ViewSetClick.getAlpha(arg0, context);
						DetailsDiao detailsDiaoFragment = new DetailsDiao(context, null, appId,regId
								,String.valueOf(userAnswerInfo.getAppId()),userAnswerInfo.getImei());
						detailsDiaoFragment.setCanceledOnTouchOutside(true);
						detailsDiaoFragment.show();
					}
				});
			} else {
				
				if (StringUtils.isNotEmpty(news)) {
					holder.the_news_content.setText(getChatData(CHString.checkMark(news)));
				} else {
					holder.the_news_content.setText(getChatData(CHString.checkMark(userAnswerInfo.getContent())));
				}
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

		return convertView;
	}

	class ViewHolder {
		public RoundImageViewExtNetw the_user_img;
		public TextView the_news_name, the_news_content, news_time;
		public TextView news_num;
		public NetworkImageView the_app_img;
	}

	private SpannableString getChatData(String msg) {
		String zhengze = "f0[0-9]{2}|f10[0-7]"; // 正则表达式，用来判断消息内是否有表情
		String newStr = CHString.checkMark(msg);
		// SpannableString spannableString =
		// ExpressionUtil.getExpressionString(context, newStr, zhengze);
		return ExpressionUtil.getExpressionString(context, newStr, zhengze);
	}

}
