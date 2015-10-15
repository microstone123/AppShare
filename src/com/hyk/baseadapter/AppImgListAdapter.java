package com.hyk.baseadapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.user.AppList;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.xmpp.XmppApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("NewApi")
public class AppImgListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<AppList> applist;
	// protected ImageLoader imageLoader = ImageLoader.getInstance();
	// protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;

	public AppImgListAdapter(Context context, List<AppList> applist) {
		super();
		this.context = context;
		this.applist = applist;
		inflater = LayoutInflater.from(context);
		// imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);

		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
	}

	@Override
	public int getCount() {
		return applist.size();
	}

	public void setData(List<AppList> applist) {
		this.applist = applist;
	}

	public AppList getData(int position) {
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
		try {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.grid_list_item, null);
				holder.dy_list_itemImage = (NetworkImageView) convertView.findViewById(R.id.dy_list_itemImage);

				// imageLoader.displayImage(applist.get(position).getAppImage(),
				// holder.dy_list_itemImage, options);
				holder.dy_list_itemImage.setErrorImageResId(R.drawable.transparent_d);
				holder.dy_list_itemImage.setDefaultImageResId(R.drawable.transparent_d);
				holder.dy_list_itemImage.setImageUrl(applist.get(position).getAppImage(), XmppApplication.getsInstance().imageLoader);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return convertView;
	}

	class ViewHolder {
		public NetworkImageView dy_list_itemImage;

	}
}
