package com.hyk.baseadapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.news.AttentionApp;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.xmpp.XmppApplication;

@SuppressLint("NewApi")
public class AttentionGridAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<AttentionApp> applist;
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;
	
	public AttentionGridAdapter(Context context, List<AttentionApp> applist) {
		super();
		this.context = context;
		this.applist = applist;
		inflater = LayoutInflater.from(context);
//		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
//		options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
	}

	@Override
	public int getCount() {
		if (applist.size() > 4) {
			return 4;
		} else {
			return applist.size();
		}
	}

	public void setData(List<AttentionApp> applist) {
		this.applist = applist;
	}

	public AttentionApp getData(int position) {
		return applist.get(position);
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public int getAppId(int position) {
		return applist.get(position).getAppId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.attention_grid_item, null);
			holder.app_attention_img = (NetworkImageView) convertView.findViewById(R.id.app_attention_img);
			holder.app_attention_name = (TextView) convertView
					.findViewById(R.id.app_attention_name);
			
//			imageLoader.displayImage(applist.get(position).getAppImage(), holder.app_attention_img,
//					options);
			holder.app_attention_img.setErrorImageResId(R.drawable.transparent_d);
			holder.app_attention_img.setDefaultImageResId(R.drawable.transparent_d);
			holder.app_attention_img.setImageUrl(applist.get(position).getAppImage(), XmppApplication.getsInstance().imageLoader);
			holder.app_attention_name.setText(applist.get(position).getAppName());
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder {
		public NetworkImageView app_attention_img;
		public TextView app_attention_name;

	}
}
