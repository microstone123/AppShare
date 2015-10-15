package com.hyk.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hyk.activity.R;
import com.hyk.utils.ImageLoaderShow;
import com.hyk.xmpp.XmppApplication;

public class GridViewAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater = null;
	private String[] imageList;
	// protected ImageLoader imageLoader;
	// private DisplayImageOptions options;
	private ImageLoaderShow imageLoaderShow;
	private RequestQueue requestQueue;

	public GridViewAdapter(Context context, String[] imageList) {
		this.mInflater = LayoutInflater.from(context);
		this.imageList = imageList;
		this.mContext = context;
		// this.imageLoader=imageLoader;
		// this.options =
		// ImageLoaderPartner.getOptions(R.drawable.transparent_d);
		requestQueue = Volley.newRequestQueue(context);
		imageLoaderShow = new ImageLoaderShow(requestQueue);
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		ViewHolder holder;
		if (contentView == null) {
			// 自定义视图
			holder = new ViewHolder();
			contentView = mInflater.inflate(R.layout.gridview_info_item, null);
			holder.mImg_info = (NetworkImageView) contentView.findViewById(R.id.mImg_info);
			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}
		try {

			// imageLoader.displayImage(imageList[position], holder.mImg_info,
			// options);
			holder.mImg_info.setDefaultImageResId(R.drawable.transparent_d);
			holder.mImg_info.setErrorImageResId(R.drawable.transparent_d);
			holder.mImg_info.setImageUrl(imageList[position], XmppApplication.getsInstance().imageLoader);

		} catch (OutOfMemoryError e) {
			// TODO: handle exception
		}
		return contentView;
	}

	static class ViewHolder {
		private NetworkImageView mImg_info;
	}

	@Override
	public int getCount() {
		return imageList.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

//	public void recycleBitmap() {
//		imageLoaderShow.recycleBitmap();
//	}
}