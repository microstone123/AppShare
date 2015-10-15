package com.hyk.baseadapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hyk.activity.R;
import com.hyk.utils.ImageLoaderPartner;
import com.hyk.view.ViewSetClick;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint("NewApi")
public class GridAppListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private String[] imag;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private boolean isNot = true;

	public GridAppListAdapter(Context context, String[] imag, boolean isNot) {
		super();
		this.context = context;
		this.imag = imag;
		inflater = LayoutInflater.from(context);
		this.isNot = isNot;
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = ImageLoaderPartner.getOptions(R.drawable.transparent_d);
	}

	@Override
	public int getCount() {
		return imag.length;
	}

	public void setData(String[] imag) {
		this.imag = imag;
	}

	public String getData(int position) {
		return imag[position];
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.dy_grid_item, null);
			holder.dy_itemImage = (ImageView) convertView.findViewById(R.id.dy_itemImage);
			holder.dy_itemImage01 = (ImageView) convertView.findViewById(R.id.dy_itemImage01);

			if (!isNot) {
				holder.dy_itemImage01.setVisibility(View.VISIBLE);
				holder.dy_itemImage.setVisibility(View.GONE);
				imageLoader.displayImage(imag[position], holder.dy_itemImage01, options);
			} else {
				holder.dy_itemImage.setVisibility(View.VISIBLE);
				holder.dy_itemImage01.setVisibility(View.GONE);
				imageLoader.displayImage(imag[position], holder.dy_itemImage, options);
			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView dy_itemImage, dy_itemImage01;

	}
}
