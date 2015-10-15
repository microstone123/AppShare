package com.hyk.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hyk.activity.R;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.view.DetailsImgGallery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GallyImageAdapter extends BaseAdapter {
	private DetailsImgGallery gallGridView;
	private LayoutInflater mInflater = null;
	private Context mContext;
	private int mPos;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private String[] imgUrlList;

	public GallyImageAdapter(Context context, String[] imgUrlList) {
		this.mInflater = LayoutInflater.from(context);
		mContext = context;
		this.imgUrlList = imgUrlList;
		
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
	}

	public void setOwnposition(int ownposition) {
		this.mPos = ownposition;
	}

	public int getOwnposition() {
		return mPos;
	}

	@Override
	public int getCount() {
		return imgUrlList.length;
	}

	@Override
	public Object getItem(int position) {
		mPos = position;
		return position;
	}

	@Override
	public long getItemId(int position) {
		mPos = position;
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mPos = position;
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.imagedialog, null);
			holder.diol_img = (ImageView) convertView.findViewById(R.id.diol_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		imageLoader.displayImage(imgUrlList[position], holder.diol_img, options);

		return convertView;
	}

	// ViewHolderæ≤Ã¨¿‡
	static class ViewHolder {
		public ImageView diol_img;
	}

}
