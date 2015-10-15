package com.hyk.baseadapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyk.activity.R;
import com.hyk.myapp.LocationApp;
import com.hyk.myapp.MyappDatabaseUtil;
import com.hyk.utils.ImageTools;
import com.hyk.view.RoundedImageView;
import com.readystatesoftware.viewbadger.BadgeView;

@SuppressLint("NewApi")
public class MoreAppListAdapter extends BaseAdapter {
	HashMap<Integer, View> lmap = new HashMap<Integer, View>();
	private Context context;
	private LayoutInflater inflater;
	private List<LocationApp> appInfoList;
	private boolean isLongClik = false;
	private MyappDatabaseUtil myappDatabaseUtil;
	private ImageView imag;
	private Handler handler;
	private static final int UNINSTALL_APK = 900001;

	private List<RelativeLayout> relatList = new ArrayList<RelativeLayout>();

	private List<BadgeView> badgeList = new ArrayList<BadgeView>();

	private List<Bitmap> bitmapList = new ArrayList<Bitmap>();

	public MoreAppListAdapter(Context context, List<LocationApp> appInfoList, Handler handler) {
		super();
		this.context = context;
		this.handler = handler;
		this.appInfoList = appInfoList;
		inflater = LayoutInflater.from(context);
		this.myappDatabaseUtil = new MyappDatabaseUtil(context);
	}

	@Override
	public int getCount() {
		return appInfoList.size();
	}

	public void setData(List<LocationApp> appInfoList) {
		if (appInfoList != null) {
			if (appInfoList.size() > 0) {
				this.appInfoList = appInfoList;
				notifyDataSetChanged();
			}
		}
	}

	public LocationApp getData(int position) {
		return appInfoList.get(position);
	}

	public List<LocationApp> getAppInfoList() {
		return appInfoList;
	}

	@Override
	public Object getItem(int position) {
		return appInfoList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void getBadgeShow(int position) {
		badgeList.get(position).show();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (lmap.get(position) == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.recent_list_item, null);
			holder.search_logo_image = (ImageView) convertView.findViewById(R.id.recent_logo_image);
			holder.search_down_num = (TextView) convertView.findViewById(R.id.recent_down_num);
			holder.search_size = (TextView) convertView.findViewById(R.id.recent_size);
			holder.search_name = (TextView) convertView.findViewById(R.id.recent_name);
			lmap.put(position, convertView);
			convertView.setTag(holder);
		} else {
			convertView = lmap.get(position);
			holder = (ViewHolder) convertView.getTag();
		}

		final LocationApp locationApp = appInfoList.get(position);
		if (locationApp != null) {

			if (locationApp.getAppDrawable() != null) {
				holder.search_logo_image.setBackground(locationApp.getAppDrawable());
			} else if (locationApp.getApp_img() != null) {
				byte[] imagequery = locationApp.getApp_img();
				// 将字节数组转化为位图
				Bitmap imagebitmap = BitmapFactory.decodeByteArray(imagequery, 0, imagequery.length);
				Drawable dr = ImageTools.bitmapToDrawable(imagebitmap);
				holder.search_logo_image.setBackground(dr);
				bitmapList.add(imagebitmap);
			}

			holder.search_name.setText(locationApp.getName());

			try {
				if (locationApp.getAppSize() != null) {
					double size = Double.valueOf(locationApp.getAppSize());
					if (size > 1024) {
						DecimalFormat df = new DecimalFormat("#.00");
						holder.search_down_num.setText(df.format(size / 1024) + "MB");
					} else {
						holder.search_down_num.setText(String.valueOf(size) + "KB");
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return convertView;
	}

	class ViewHolder {
		public ImageView search_logo_image;
		public TextView search_down_num, search_size;
		public TextView search_name;
	}

	public void recycleBitmap() {
		if (bitmapList != null) {
			for (int i = 0; i < bitmapList.size(); i++) {
				if (bitmapList.get(i) != null) {
					bitmapList.get(i).recycle();
				}
			}
		}
	}
}
